package org.choongang.admin.member.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.ListData;
import org.choongang.member.entities.Member;
import org.choongang.member.service.MemberInfoService;
import org.choongang.menus.Menu;
import org.choongang.menus.MenuDetail;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller("adminMemberController")
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class MemberController implements ExceptionProcessor {

    private final MemberInfoService infoService;

    @ModelAttribute("menuCode")
    public String getMenuCode() {
        return "member";
    }

    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() {

        return Menu.getMenus("member");
    }


    @GetMapping
    public String list(@ModelAttribute  MemberSearch search, Model model) {
        commonProcess("list", model);

        ListData<Member> data = infoService.getList(search);

        model.addAttribute("items", data.getItems()); // 목록
        model.addAttribute("pagination", data.getPagination()); // 페이징

        return "admin/member/list";
    }

    private void commonProcess(String mode, Model model) {
        mode = StringUtils.hasText(mode) ? mode : "list";
        String pageTitle = "운영마당";
        List<String> addScript = new ArrayList<>();

        if (mode.equals("list")) {
            pageTitle = "회원 관리::" + pageTitle;
        }


        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addScript", addScript);
        model.addAttribute("subMenuCode", "member_" + mode);
    }
}
