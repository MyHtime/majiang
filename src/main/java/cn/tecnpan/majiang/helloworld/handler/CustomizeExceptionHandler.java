package cn.tecnpan.majiang.helloworld.handler;

import cn.tecnpan.majiang.helloworld.dto.ResultDto;
import cn.tecnpan.majiang.helloworld.enums.CustomizeErrorEnum;
import cn.tecnpan.majiang.helloworld.exception.CustomizeException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice
@Slf4j
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handleControllerException(HttpServletRequest request, Throwable ex, Model model, HttpServletResponse response) {
        String contentType = request.getContentType();
        if (contentType != null && (contentType.toLowerCase()).contains("application/json")) {
            ResultDto resultDto;
            //json
            if (ex instanceof CustomizeException) {
                resultDto =  ResultDto.errorOf((CustomizeException) ex);
            } else {
                log.error("handle error", ex);
                resultDto =  ResultDto.errorOf(CustomizeErrorEnum.SYSTEM_ERROR);
            }
            try {
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");
                response.setStatus(200);
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDto));
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            //view
            if (ex instanceof CustomizeException) {
                model.addAttribute("messages", ex.getMessage());
            } else {
                log.error("handle error", ex);
                model.addAttribute("messages", CustomizeErrorEnum.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
