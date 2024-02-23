package org.choongang.board.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.board.entities.Notice_;
import org.choongang.board.entities.Qna;
import org.choongang.board.service.QnaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService qnaService;

    /* QnA 게시글 등록(이용자, 관리자 권한) S */
    @GetMapping("/add")
    public String qnaAdd(Model model) {
        model.addAttribute("RequestQnaAdd", new RequestQnaAdd());

        return "board/qnaAdd";
    }

    @PostMapping("/add")
    public String qnaList(RequestQnaAdd form, Model model) {

        qnaService.save(form);

        return "redirect:/board/qna/list";
    }
    /* QnA 게시글 등록(이용자, 관리자 권한) E */


    /* QnA 게시글 목록 조회 S */
    @GetMapping("/qnaList")
    public String list(@ModelAttribute Qna form, Model model) {

        List<Qna> qnaList = qnaService.getList();
        model.addAttribute("qnaList", qnaList);

        return "board/qnaList";
    }

    @PostMapping("/list")
    public String qnaList() {

        return "redirect:/board/qna/add";
    }

    /* QnA 게시글 목록 조회 E */

    /* QnA 게시글 상세 조회 S */

    @GetMapping("/qnaDetail/{num}")
    public String detail(@PathVariable Long num, Model model) {

        // 경로 변수 num이 null이거나 음수인 경우에는 admin/board/qnaList로 리다이렉션
        if (num <= 0) {
            return "redirect:/admin/board/qnaList";
        }

        // 게시글 번호를 사용하여 해당 게시글 정보를 가져온다.
        Optional<Qna> qnaDetail = qnaService.qnaFindByNum(num);

        // 게시글이 존재하는 경우에는 모델에 추가하고 admin/board/qnaDetail 페이지를 반환
        if (qnaDetail.isPresent()) {
            model.addAttribute("qnaDetail", qnaDetail.get());
            model.addAttribute("requestQnaAdd", new RequestQnaAdd());
            return "admin/board/qnaDetail";
        }

        // 해당 게시글을 찾을 수 없는 경우에는 admin/board/qnaList로 리다이렉션
        return "redirect:/admin/board/qnaList";
    }

    /* QnA 게시글 상세 조회 E */

    /* QnA 게시글 수정 S */

    @GetMapping("/qnaEdit/{num}")
    public String editForm(@PathVariable Long num, Model model) {

        // 게시글 번호를 사용하여 해당 게시글 정보를 가져온다.
        Optional<Qna> qnaDetail = qnaService.qnaFindByNum(num);

        // 게시글이 존재하는 경우에는 모델에 추가하고 admin/board/qnaEdit 페이지를 반환
        if (qnaDetail.isPresent()) {
            model.addAttribute("requestQnaAdd", qnaDetail.get());

            return "admin/board/qnaEdit";
        }

        // 해당 게시글을 찾을 수 없는 경우에는 admin/board/qnaList로 리다이렉션
        return "redirect:/guid/qna/list";
    }

    @PostMapping("/qnaEdit")
    public String editqna(RequestQnaAdd form, Model model) {

        // 기존 게시물 정보 가져오기
        Optional<Qna> existingQna = qnaService.qnaFindByNum(form.getNum());

        // 기존 게시물이 존재하는 경우에만 수정
        if (existingQna.isPresent()) {
            Qna qna = existingQna.get();

            // 기존 게시물 내용 수정
            qna.setType(form.getType());
            qna.setTitle(form.getTitle());
            qna.setAnswerAlert(form.getAnswerAlert());
            qna.setContent(form.getContent());
            qna.setFileName(form.getFileName());
            qna.setFileAddress(form.getFileAddress());

            // 수정된 게시물 저장
            qnaService.save(form);

        }

        return "redirect:/guid/qna/list";
    }

    /* QnA 게시글 수정 E */

    /* QnA 게시글 삭제 S */

    @GetMapping("/qnaDelete/{num}")
    public String deleteQna(@PathVariable Long num) {
        qnaService.deleteById(num);
        return "redirect:/guid/qna/list";
    }

    /* QnA 게시글 삭제 E */


}