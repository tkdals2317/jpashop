package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("api/v1/members")
    public CreateMemberResponse saveMemeberV1(@RequestBody @Valid Member member) {
        Long memberId = memberService.join(member);
        return new CreateMemberResponse(memberId);
    }

    /**
     * 엔티티와 요청객체의 분리
     *
     * @param request
     * @return
     */
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemeberV2(@RequestBody @Valid CreateMemberRequest request) {
        Long memberId = memberService.join(request.toEntity());
        return new CreateMemberResponse(memberId);
    }

    @PutMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @GetMapping("api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    @Getter
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;

        public Member toEntity() {
            Member member = new Member();
            member.setName(getName());
            return member;
        }
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @Data
    @Getter
    static class CreateMemberRequest {
        @NotEmpty
        private String name;

        public Member toEntity() {
            Member member = new Member();
            member.setName(getName());
            return member;
        }
    }
}
