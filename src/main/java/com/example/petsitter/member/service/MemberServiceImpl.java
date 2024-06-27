package com.example.petsitter.member.service;

import com.example.petsitter.member.domain.Member;
import com.example.petsitter.member.domain.MemberRole;
import com.example.petsitter.member.dto.CustomUserDetails;
import com.example.petsitter.member.dto.MemberDto;
import com.example.petsitter.member.repository.MemberRepository;
import com.example.petsitter.core.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
@Log4j2
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final CustomFileUtil fileUtil;

    @Override
    public void joinProcess(MemberDto memberDto) {
        //db에 동일한 username이 존재하는지 확인
        boolean isUser = memberRepository.existsByEmail(memberDto.getEmail());

        if(isUser){
            return;
        }

        //비밀번호 인코딩
        String encodedPassword = bCryptPasswordEncoder.encode(memberDto.getPassword());
        memberDto.setPassword(encodedPassword);

        // 파일 업로드
        List<MultipartFile> files = memberDto.getFiles();
        List<String> uploadedFileNames = fileUtil.saveFiles(files);
        memberDto.setUploadedFileName(uploadedFileNames);

        // 저장
        memberRepository.save(memberDto.toEntity());
    }

    @Override
    public String idCheck(String email) {
        boolean isUser = memberRepository.existsByEmail(email);
        boolean check = false;

        String msg= "";
        if(isUser){
            msg = "이미 사용중인 아이디 입니다!";
        }else {
            msg = "사용 가능합니다!";
            check = true;
        }

        return msg;
    }

    @Override
    public void updateMember(MemberDto memberDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        MemberDto oldDto = findByDtoEmail(username);
        Member member = memberRepository.findByEmail(username);

        //기존의 파일들
        List<String> oldFileNames = oldDto.getUploadedFileName();

        //새로 업로드할 파일
        List<MultipartFile> files = memberDto.getFiles();

        //새로 업로드 해야하는 파일들
        List<String> currentUploadFileNames = fileUtil.saveFiles(files);


        // 화면에서 변화 없이 유지 되어야할 파일들
        List<String> uploadedFileNames = memberDto.getUploadedFileName();

        //유지되는 파일들  + 새로 업로드된 파일 이름들이 저장해야 하는 파일 목록이 됨
        if(currentUploadFileNames != null && currentUploadFileNames.size() > 0) {
            uploadedFileNames.addAll(currentUploadFileNames);
        }

        member.updateFromDTO(memberDto);
        if(member.isSocial() != true){
            member.clearList();
            List<String> uploadFileNames = memberDto.getUploadedFileName();

            if(uploadFileNames != null && uploadFileNames.size() > 0 ){
                uploadFileNames.stream().forEach(uploadName -> {
                    member.addImageString(uploadName);
                });
            }
        }
        memberRepository.save(member);

        if(oldFileNames != null && oldFileNames.size() > 0){
            if(member.isSocial() != true) {
            // 지워져야하는 파일 목록 찾기
            // 예전 파일들 중에 지워져야하는 파일 이름들
            List<String> removeFiles = oldFileNames
                    .stream()
                    .filter(fileNeme -> uploadedFileNames.indexOf(fileNeme) == -1).collect(Collectors.toList());


                // 실제 파일 삭제
                fileUtil.deleteFiles(removeFiles);
            }
        }
    }

    @Override
    public Member findByEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        return member;
    }

    @Override
    public MemberDto findByDtoEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            return null; // or handle the case where member is not found
        }
        MemberDto memberDto = MemberDto.tomemberDto(member);

        return memberDto;
    }

    @Override
    public String getAuthName() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return username;
    }

    @Override
    public void addRole() {
        String username = getAuthName();
        Member member = memberRepository.findByEmail(username);
        member.addRole(MemberRole.MANAGER);
        refreshAuthentication(member.getEmail());
    }

    @Override
    public void clearRole() {
        String username = getAuthName();
        Member member = memberRepository.findByEmail(username);
        member.clearRole();
        member.addRole(MemberRole.USER);
        refreshAuthentication(member.getEmail());
    }

    @Override
    public void refreshAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public Member saveAddress(String address, String detailAdr, Long id) {
        Optional<Member> memberOptional = memberRepository.findById(id);

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            member.updateAddressDTO(address, detailAdr);

            memberRepository.save(member);
            return member;
        } else {
            return null;
        }

    }
}
