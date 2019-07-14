package cn.tecnpan.majiang.helloworld.service;

import cn.tecnpan.majiang.helloworld.dto.NotificationDto;
import cn.tecnpan.majiang.helloworld.dto.PaginationDto;
import cn.tecnpan.majiang.helloworld.model.User;

public interface NotificationService {

    PaginationDto<NotificationDto> list(Long userId, Integer pageNo, Integer pageSize);

    Long countUnreadNotify(Long userId);

    NotificationDto readNotification(Long id, User user);
}
