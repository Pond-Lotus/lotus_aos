# 토도리(TODORI)
심플하고 간편한 투두리스트
매일 할 일을 쉽게 정리할 수 있는 투두리스트 앱\
작은 도토리를 모으듯이, 오늘의 할일을 차곡차곡 완료해 보세요!

![토도리(TODORI) 그래픽이미지](https://github.com/Pond-Lotus/lotus_aos/assets/51289286/dcb26e5b-9e62-4314-a012-41985ed2745f)

<br/>

# 기술 스택
- Android
- Jetpack Compose
- Retrofit
- ViewModel
- Repository
- DataSource
- Module
- Dagger & Hilt

<br/>

# 디자인 패턴
MVVM(Model + View + ViewModel)

<br/>

# 문제 발생 및 해결 과정
1. api 통신 속도 개선
    - **문제**: 리스트 CRUD 요청 시 데이터 응답 속도 및 컴포저블 렌더링 속도 저하
    - **원인**: api 함수를 컴포저블 함수 내에서 구현하여 데이터 상태 관리 및 api 통신 속도 저하
    - **해결**: 데이터 상태 관리, 데이터 접근 로직 캡슐화, 위존성 주입 등 MVVM 디자인 패턴 도입으로 api 통신 속도 개선
    - **결과**:  api 응답 속도 약 5초 → 1초 이내 감소(**80% 감소**)
2. 빌드 속도 개선
    - **문제**: 앱 빌드 시 약 15초 이상의 시간이 소모
    - **원인**: MVVM 패턴에 맞게 의존성 규칙을 정했지만 많은 양의 코드와 복잡한 구조로 인한 빌드 시간 증가
    - **해결**: 도메인 지식을 캡슐화하는 방향으로 결정하여, Module을 활용해 기능 단위를 분리하고, 기능 간에 공통으로 사용되는 모듈을 추가적으로 분리함.
    - 결과: 빌드 시간 약 15초 → 약 10초 이내 감소(**33% 감소**)

<br/>

# 스크린샷
<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/470a9f5b-2a54-4521-ab13-0b307ad47fa5" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/b6790d20-147c-4573-9193-342afa2e66ba" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/58b2b016-f466-4b1c-96ef-c8d59ba9145f" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/3ceb2eab-9883-43d6-ba62-256642fce9fb" width="200"/></td>
  </tr>
  <tr>
    <td align="center">로그인 스크린</td>
    <td align="center">월간 리스트 스크린</td>
    <td align="center">리스트 추가 및 수정 스크린</td>
    <td align="center">프로필 스크린</td>
  </tr>
</table>

<br/>

# 성과
Google Play Store 에 앱 출시 및 30명 유저 확보(현재는 서비스 중단)

<br/>

# 스토어
스토어 링크: ~~[링크](https://play.google.com/store/apps/details?id=com.lotus.todo_android&hl=kr)~~(현재는 서비스 중단)
