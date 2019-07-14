package cn.tecnpan.majiang.helloworld.service.impl;

import cn.tecnpan.majiang.helloworld.dto.NotificationDto;
import cn.tecnpan.majiang.helloworld.dto.PaginationDto;
import cn.tecnpan.majiang.helloworld.enums.CustomizeErrorEnum;
import cn.tecnpan.majiang.helloworld.enums.NotificationStatusEnum;
import cn.tecnpan.majiang.helloworld.enums.NotificationTypeEnum;
import cn.tecnpan.majiang.helloworld.exception.CustomizeException;
import cn.tecnpan.majiang.helloworld.mapper.NotificationMapper;
import cn.tecnpan.majiang.helloworld.model.Notification;
import cn.tecnpan.majiang.helloworld.model.NotificationExample;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.service.NotificationService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private NotificationMapper notificationMapper;


    /**
     * 查询当前用户下的未读通知列表
     * @param userId 当前登录用户的ID
     */
    @Override
    public PaginationDto<NotificationDto> list(Long userId, Integer pageNo, Integer pageSize) {
        PaginationDto<NotificationDto> pagination = new PaginationDto<>((int)notificationMapper.countByExample(new NotificationExample()), pageSize);
        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > pagination.getTotalPage()) {
            pageNo = pagination.getTotalPage();
        }
        Integer offset = pageSize * (pageNo - 1);
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId);
        notificationExample.setOrderByClause("gmt_create desc");

        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, pageSize));
        if (notificationList.size() == 0) {
            return pagination;
        }

//        List<NotificationDto> notificationDtoList = new ArrayList<>();
//        for (Notification notification : notificationList) {
//            NotificationDto notificationDto = new NotificationDto();
//            BeanUtils.copyProperties(notification, notificationDto);
//            notificationDto.setNotifyTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
//            notificationDtoList.add(notificationDto);
//        }

        List<NotificationDto> notificationDtoList = notificationList.stream().map(notification -> {
            NotificationDto notificationDto = new NotificationDto();
            BeanUtils.copyProperties(notification, notificationDto);
            notificationDto.setNotifyTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
            return notificationDto;
        }).collect(Collectors.toList());

        pagination.setObjectList(notificationDtoList);
        if (pagination.getTotalPage() != 0) {
            pagination.init(pageNo);
        }
        return pagination;
    }

    /**
     * 询当前用户下的未读通知的总数
     * @param userId 当前登录用户的ID
     */
    @Override
    public Long countUnreadNotify(Long userId) {
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(0);
        notificationExample.setOrderByClause("gmt_create desc");
        return notificationMapper.countByExample(notificationExample);
    }

    /**
     * 阅读一条通知（登录的用户点击一条通知跳转到通知发生的页面，并阅读这条通知）
     * @param id 通知的ID
     * @param user 登录的用户
     */
    @Override
    public NotificationDto readNotification(Long id, User user) {
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        //消息不存在
        if (notification == null) {
            throw new CustomizeException(CustomizeErrorEnum.QUESTION_NOT_FOUND);
        }
        //判断通知接收对象是不是登录的用户
        if (!notification.getReceiver().equals(user.getId())) {
            throw new CustomizeException(CustomizeErrorEnum.READ_NOTIFICATION_FAIL);
        }

        NotificationDto notificationDto = new NotificationDto();
        BeanUtils.copyProperties(notification, notificationDto);
        notificationDto.setNotifyTypeName(NotificationTypeEnum.nameOfType(notification.getType()));

        //更新为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKeySelective(notification);

        return notificationDto;
    }
}
