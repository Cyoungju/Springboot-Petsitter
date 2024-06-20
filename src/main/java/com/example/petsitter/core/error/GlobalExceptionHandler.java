package com.example.petsitter.core.error;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GlobalExceptionHandler implements ErrorController {
    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String handleError(Model model, HttpServletRequest request) {
        // 에러 상태 코드를 가져옵니다.
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // HTTP 상태 코드별로 에러 메시지를 설정합니다.
            if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                model.addAttribute("errorStatus", statusCode);
                model.addAttribute("errorMsg", "요청이 잘못되었습니다.");
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                model.addAttribute("errorStatus", statusCode);
                model.addAttribute("errorMsg", "인증이 필요합니다.");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("errorStatus", statusCode);
                model.addAttribute("errorMsg", "접근 권한이 없습니다.");
            } else if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("errorStatus", statusCode);
                model.addAttribute("errorMsg", "페이지가 없습니다.");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("errorStatus", statusCode);
                model.addAttribute("errorMsg", "서버에서 오류가 발생했습니다.");
            } else {
                model.addAttribute("errorStatus", statusCode);
                model.addAttribute("errorMsg", "예기치 않은 오류가 발생했습니다.");
            }
        }

        return "error"; // error.html을 템플릿으로 사용합니다.
    }

    public String getErrorPath() {
        return PATH;
    }

}
