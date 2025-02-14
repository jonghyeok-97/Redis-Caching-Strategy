package cache.strategy.service;

import cache.strategy.domain.Member;
import cache.strategy.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final ExternalService externalService;

    /**
     * @Cacheable 동작 방식
     *      1. 메서드가 실행될 때마다 캐시 적용
     *      2. 매개변수로 메서드가 호출된적 있는지 확인
     *      3. 기본적으로 메서드의 매개변수를 키로 사용해서 캐시를 조회
     *
     *      + 반환 타입이 Optional인 경우
     *          - 값이 있으면 Optional 내부의 값 저장
     *          - 값이 없으면 null 저장
     *  key : Member::1
     */
    @Cacheable(cacheNames = "Member", key = "#id")
    public Member findById(Long id) {
        log.info("[findById] 호출");
        externalService.callExternal();
        return memberRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }
}
