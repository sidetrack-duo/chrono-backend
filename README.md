<p align="center">
  <img src="./assets/images/chrono.png" width="420" alt="Chrono"/>
</p>


# 📌 프로젝트 개요

GitHub 커밋 기반 사이드 프로젝트 트래커  
개발자의 프로젝트 진행 현황을 커밋 데이터로 시각화하고 관리하는 서비스


## 배포 주소

- https://app.chrono.name
- 로그인 화면에서 데모 계정으로 이용


## 팀 소개
<table align="center">
  <tr>
    <td align="center" style="padding:20px;">
      <strong style="font-size:18px;">Frontend</strong><br/><br/>
      <a href="https://github.com/MA-Ha-eun">
        <img src="https://github.com/MA-Ha-eun.png" width="96"/><br/><br/>
        <strong>마하은</strong>
      </a>
    </td>

    <td align="center" style="padding:20px;">
      <strong style="font-size:18px;">Backend</strong><br/><br/>
      <a href="https://github.com/simuneu">
        <img src="https://github.com/simuneu.png" width="96"/><br/><br/>
        <strong>박시현</strong>
      </a>
    </td>
  </tr>
</table>



## 주요 기능
<details>
  <summary><strong> 인증 · 사용자 관리</strong></summary>
  <br/>

- 이메일 인증 코드 발송 및 검증을 통한 회원가입
- JWT 기반 인증 (Access Token / Refresh Token 분리)
- Refresh Token은 HttpOnly Cookie 방식으로 관리하여 보안 강화
- 비밀번호 변경 / 재설정(이메일 인증) / 회원 탈퇴 지원
- 내 정보 조회 및 닉네임 수정 기능 제공

</details>

<details>
  <summary><strong> GitHub 연동 (Basic / PAT)</strong></summary>
  <br/>

- GitHub 사용자명 유효성 검사 및 프로필 정보 조회
- **Basic 연동**
    - 퍼블릭 레포지토리 조회 지원
- **PAT 연동**
    - Private Repository 포함 전체 레포 접근
    - Personal Access Token 암호화 저장
- PAT 연동 해제 기능 제공으로 보안 및 계정 관리 유연성 확보

</details>

<details>
  <summary><strong> 프로젝트 관리</strong></summary>
  <br/>

- GitHub Repository 기반 프로젝트 등록
- 프로젝트 메타 정보 관리
    - 제목, 설명, 기술 스택, 시작일 / 목표일 설정
- 프로젝트 상태 관리
    - 진행 중 / 완료 상태 전환
- 프로젝트 활성 / 비활성(소프트 삭제) 지원
- 커밋 데이터 기반 프로젝트 진행률 자동 계산

</details>

<details>
  <summary><strong> 커밋 수집 · 분석</strong></summary>
  <br/>

- GitHub 커밋 데이터 수동 동기화
- 프로젝트별 커밋 통계 제공
    - 전체 커밋 수
    - 최근 커밋 날짜
    - 주간 커밋 분포
    - 기간별 커밋 히스토리
- **Python 분석 서버 분리**
    - 통계 및 집계 로직을 Python 서버에서 처리
    - Java 서버의 책임 최소화 및 분석 확장성 확보

</details>

<details>
  <summary><strong> 대시보드</strong></summary>
  <br/>

- 사용자 기준 프로젝트 요약 정보 제공
    - 진행 중 / 완료 프로젝트 수
    - 이번 달 총 커밋 수
- 주간 커밋 데이터 시각화를 위한 통계 API 제공
- 최근 활동 프로젝트 요약 정보 제공

</details>

<details>
  <summary><strong> 설계적 특징</strong></summary>
  <br/>

- REST API 기반의 명확한 책임 분리
- Refresh Token 쿠키 저장 방식으로 인증 보안 강화
- 소프트 삭제 전략 적용으로 데이터 유실 방지
- GitHub API / 분석 서버 / 프론트엔드 간 독립적 확장 구조

</details>


## 기술 스택
### Frontend
![React](https://img.shields.io/badge/React-61DAFB?logo=react&logoColor=white)
![React Router](https://img.shields.io/badge/React_Router-CA4245?logo=reactrouter&logoColor=white)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?logo=typescript&logoColor=white)
![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-38B2AC?logo=tailwindcss&logoColor=white)
![Zustand](https://img.shields.io/badge/Zustand-State_Management-FF6A00?logoColor=white)
![Axios](https://img.shields.io/badge/Axios-HTTP_Client-5A29E4?logo=axios&logoColor=white)
![Vite](https://img.shields.io/badge/Vite-Bundler-646CFF?logo=vite&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?logo=css3&logoColor=white)

### Backend
![Java](https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?logo=springsecurity&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-Hibernate-59666C?logo=hibernate&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-EA1D2C?logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A?logo=gradle&logoColor=white)
![Python](https://img.shields.io/badge/Python-3.x-3776AB?logo=python&logoColor=white)
![FastAPI](https://img.shields.io/badge/FastAPI-009688?logo=fastapi&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)


### Infrastructure
![AWS EC2](https://img.shields.io/badge/AWS_EC2-Ubuntu-FF9900?logo=amazonaws&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Container-2496ED?logo=docker&logoColor=white)
![Docker Compose](https://img.shields.io/badge/Docker_Compose-Orchestration-2496ED?logo=docker&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-Reverse_Proxy-009639?logo=nginx&logoColor=white)
![Certbot](https://img.shields.io/badge/Certbot-SSL-003A70?logo=letsencrypt&logoColor=white)
![Vercel](https://img.shields.io/badge/Vercel-Deploy-000000?logo=vercel&logoColor=white)

## 시스템 설계 & 문서
<details>
  <summary><strong> 시스템 / 배포 아키텍처</strong></summary>
  <br/>
  <p align="center">
    <img src="./assets/images/Architecture.png" width="700"/>
  </p>
</details>

<details>
  <summary><strong> ERD</strong></summary>
  <br/>
  <p align="center">
    <img src="./assets/images/chrono-erd.png" width="700"/>
  </p>
</details>

<details>
  <summary><strong> API 문서</strong></summary>
  <br/>
  <a href="./api.md"> API Documentation 바로가기</a>
</details>
