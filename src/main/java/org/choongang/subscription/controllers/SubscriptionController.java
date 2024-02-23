package org.choongang.subscription.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.education.controllers.EduDataSearch;
import org.choongang.admin.education.entities.EduData;
import org.choongang.admin.education.service.EduDataInfoService;
import org.choongang.admin.gamecontent.controllers.GameContentSearch;
import org.choongang.admin.gamecontent.entities.GameContent;
import org.choongang.admin.gamecontent.service.GameContentInfoService;
import org.choongang.admin.gamecontent.service.GameContentSaveService;
import org.choongang.commons.ListData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/subscription")
@RequiredArgsConstructor
@SessionAttributes({"items", "totalPayment"})
public class SubscriptionController {

    private final EduDataInfoService eduDataInfoService;
    private final GameContentInfoService gameContentInfoService;
    private final OrderValidator orderValidator;

    /* 구매한 게임콘텐츠 목록 */
    @GetMapping
    public String list(@ModelAttribute GameContentSearch search,
                       Model model) {
        commonProcess("list", model);

        ListData<GameContent> data = gameContentInfoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());;

        return "subscription/list";
    }

    /* 게임컨텐츠 구매 할 수 있는 리스트*/
    @GetMapping("/subscribe")
    public String subscribe(@ModelAttribute GameContent form,
                            GameContentSearch search,
                            Model model) {
        commonProcess("apply", model);

        ListData<GameContent> data = gameContentInfoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return "subscription/subscribe";
    }

    /* 게임컨텐츠 결제 */
    @PostMapping("/apply")
    public String apply(@ModelAttribute RequestOrder form, Model model,
                        @RequestParam(name = "chk") List<Long> nums) {
        commonProcess("apply", model);

        Map<String, Object> data = gameContentInfoService.getOrderSummary(nums);
        model.addAllAttributes(data);


        return "subscription/apply";
    }

    @PostMapping("/applyPs")
    public String applyPs(@Valid RequestOrder form, Errors errors, Model model, SessionStatus status) {

        orderValidator.validate(form, errors);

        if (errors.hasErrors()) {
            return "subscription/apply";
        }

        // 주문 접수 처리

        status.setComplete();

        return "redirect:/subscription";
    }

    /* 관리자가 등록한 교육 자료 리스트 */
    @GetMapping("/eduList")
    public String eduList(@ModelAttribute EduDataSearch search, Model model) {
        commonProcess("list", model);

        ListData<EduData> data = eduDataInfoService.getList(search);
        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return "subscription/eduList";
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "list";
        String pageTitle = "구독서비스";

        List<String> addScript = new ArrayList<>();

        if (mode.equals("list")) {
            pageTitle = "내 게임콘텐츠 조회::" + pageTitle;
        } else if (mode.equals("apply")) {
            pageTitle = "게임콘텐츠 구매::" + pageTitle;
        }

        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addScript", addScript);
        model.addAttribute("subMenuCode", mode);
    }
}
