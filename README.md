## auth<br/>

### 이메일 인증코드 발송<br/>
POST /api/auth/email/send<br/>
{<br/>
    "email": "user@example.com"<br/>
}<br/>

### 이메일 인증코드 확인<br/>

POST /api/auth/email/verify<br/>

{<br/>
    "email": "user@example.com",<br/>
    "code": "A1B2C3D4"<br/>
}<br/>

(이메일 템플릿 미적용)<br/>

### 회원가입<br/>
POST /api/auth/signup<br/>

{<br/>
    "email": "user@example.com",<br/>
    "password" : "abcd1234!",<br/>
    "nickname" :"다람띠"<br/>
}<br/>
조건 : 비밀번호는 영문, 숫자, 특수문자 포함 8자 이상<br/>


### 로그인<br/>
POST /api/auth/login<br/>
{<br/>
    "email": "user@example.com",<br/>
    "password" : "abcd1234!",<br/>
}<br/>
Refresh Token → HttpOnly Cookie방식<br/>

### refreshtoken으로 accesstoken재발급<br/>
POST api/auth/refresh<br/>
eyJhbGciOiJIUzIOTgsImV4cCI6MTc2NDkyNjY~~~~~~~<br/>

### 로그아웃<br/>
POST api/auth/logout<br/>

### 회원 탈퇴
DELETE api/auth<br/>
성공<br/>
{<br/>
"success": true,<br/>
"message": "SUCCESS",<br/>
"data": null<br/>
}<br/>
## github<br/>
### 유효한 깃허브 유저네임인지 조회<br/>
GET api/github/validate<br/>
http://localhost:8080/api/github/validate?username=simuneu<br/>
성공시<br/>
{<br/>
"success": true,<br/>
"message": "SUCCESS",<br/>
"data": {<br/>
"valid": true,<br/>
"username": "simuneu",<br/>
"avatarUrl": "https://github.com/simuneu.png",<br/>
"message": "존재하는 GitHub 사용자입니다."<br/>
}<br/>


### 기본 연동<br/>
POST api/github/connect-basic<br/>
요청<br/>
{<br/>
"username": "simuneu"<br/>
}<br/>
성공 응답<br/>
{<br/>
"connected": true,<br/>
"type": "BASIC",<br/>
"username": "simuneu",<br/>
"avatarUrl": "https://avatars.githubusercontent.com/u/191446770?v=4",<br/>
"message": "기본 연동이 완료되었습니다."<br/>
}<br/>

### pat 연동<br/>
POST api/github/connect-pat<br/>
요청<br/>
{<br/>
"username": "simuneu",<br/>
"pat":"pat입력"<br/>
}<br/>
성공 응답<br/>
{<br/>
"connected": true,<br/>
"type": "FULL",<br/>
"message": "github full연동 완료"<br/>
}<br/>
pat - 암호화되어 저장<br/>
#### pat생성 안내 가이드<br/>
https://github.com/settings/tokens?type=beta 이동<br/>
[Generate new token] 버튼을 클릭<br/>
토큰 이름(Name)을 입력<br/>
만료 기간(Expiration)을 선택<br/>
Repository access 선택 , All repositories 또는 필요한 레포만 선택<br/>
#### Repository permissions<br/>
Contents: Read-only<br/>
Metadata: Read-only<br/>
#### User permissions<br/>
Email addresses: Read-only<br/>
Profile: Read-only<br/>
페이지 하단에서 **[Generate token]**을 클릭합니다.<br/>
생성된 토큰 값을 입력 안내<br/>

### pat 연동 해제<br/>
DELETE api/github/pat<br/>
성공응답<br/>
{<br/>
"success": true,<br/>
"message": "SUCCESS",<br/>
"data": {<br/>
"connected": true,<br/>
"type": "BASIC",<br/>
"message": "PAT연동 해제"<br/>
}<br/>
}<br/>

### 레포 가져오기<br/>
GET api/github/repos<br/>
요청 body없음(토큰만 필요)<br/>
응답 예시<br/>
[<br/>
{<br/>
"repoId": 1059981952,<br/>
"repoName": "bid-N-buy-backend",<br/>
"fullName": "bid-N-buy/bid-N-buy-backend",<br/>
"description": "[Bid&Buy] 중고거래에 실시간 경매를 더한 새로운 거래 서비스",<br/>
"htmlUrl": "https://github.com/bid-N-buy/bid-N-buy-backend",<br/>
"language": "Java",<br/>
"stargazersCount": 0,<br/>
"forksCount": 1,<br/>
"updatedAt": "2025-11-27T15:21:31Z",<br/>
"private": false<br/>
},<br/>
{<br/>
"repoId": 1059975363,<br/>
"repoName": "bid-N-buy-frontend",<br/>
"fullName": "bid-N-buy/bid-N-buy-frontend",<br/>
"description": "[Bid&Buy] 중고거래에 실시간 경매를 더한 새로운 거래 서비스",<br/>
"htmlUrl": "https://github.com/bid-N-buy/bid-N-buy-frontend",<br/>
"language": "TypeScript",<br/>
"stargazersCount": 0,<br/>
"forksCount": 0,<br/>
"updatedAt": "2025-11-28T07:30:05Z",<br/>
"private": false<br/>
}<br/>
]<br/>

## project<br/>

### 프로젝트 등록<br/>
POST api/projects<br/>
요청<br/>
{<br/>
"owner": "simuneu",<br/>
"repoName": "front-practice",<br/>
"repoUrl": "https://github.com/simuneu/front-practice"<br/>
}<br/>
--현재 pat설정 경우 퍼블릭, 프라이빗 둘 다 등록 가능,오가닉에 관한 부분은 다시 확인 필요<br/>

### 프로젝트 필드 직접 등록<br/>
PUT api/projects/{projectId}/meta<br/>
요청값<br/>
{<br/>
"title": "프로젝트",<br/>
"description": "프로젝트트 설명",<br/>
"techStack": ["Spring Boot", "MySQL", "Redis", "FCM"],<br/>
"startDate": "2025-12-01",<br/>
"targetDate": "2025-12-31"<br/>
}<br/>

### 프로젝트 리스트 조회 -- 진행률 포함<br/>
GET api/projects<br/>
성공 응답<br/>
{<br/>
"success": true,<br/>
"message": "SUCCESS",<br/>
"data": [<br/>
{<br/>
"projectId": 1,<br/>
"owner": "simuneu",<br/>
"repoName": "chrono",<br/>
"repoUrl": "https://github.com/simuneu/chrono",<br/>
"active": true,<br/>
"createdAt": "2025-12-07T23:23:40.649786",<br/>
"title": null,<br/>
"status": "COMPLETED",<br/>
"techStack": [],<br/>
"totalCommits": null,<br/>
"lastCommitAt": null,<br/>
"startDate": null,<br/>
"targetDate": null<br/>
"progressRate": 53<br/>
}]}<br/>

### 프로젝트 상태 변경<br/>
PATCH /api/projects/{id}/status<br/>
요청<br/>
{<br/>
"status": "COMPLETED"<br/>
}<br/>

### 프로젝트 상세조회<br/>
GET /api/projects/{id}<br/>
성공응답<br/>
{<br/>
"projectId": 4,<br/>
"owner": "simuneu",<br/>
"repoName": "budgie_backend",<br/>
"repoUrl": "https://github.com/simuneu/budgie_backend",<br/>
"title": "프로젝트",<br/>
"description": "프로젝트트 설명",<br/>
"techStack": [<br/>
"Spring Boot",<br/>
"MySQL",<br/>
"Redis",<br/>
"FCM"<br/>
],<br/>
"startDate": "2025-12-01",<br/>
"targetDate": "2025-12-31",<br/>
"status": "COMPLETED",<br/>
"active": true,
"createdAt": "2025-12-13T19:38:57.93523",<br/>
"totalCommit": 94,<br/>
"lastCommitAt": "2025-12-10T08:49:55"<br/>
}<br/>

### 프로젝트 활성, 비활성<br/>
PATCH /api/projects/{projectId}/active<br/>
요청<br/>
{<br/>
"active": false<br/>
}<br/>
true면 활성화, false면 비활성화(소프트 딜리트)<br/>



## Commit<br/>

### 커밋 동기화<br/>
POST api/projects/{projectId}/commits/sync<br/>
성공 응답<br/>
{<br/>
"message": "커밋 동기화 완료",<br/>
"savedCount": 12<br/>
}<br/>

### 커밋 수 전체 조회<br/>
GET api/projects/{projectId}/commits/count<br/>
성공 응답<br/>
{<br/>
"projectId": 2,<br/>
"totalCommits": 12<br/>
}<br/>

### 최근 커밋 날짜 조회<br/>
GET api/projects/{projectId}/commits/latest<br/>
성공 응답<br/>
{<br/>
"latestCommitDate": "2025-07-20T12:16:35",<br/>
"projectId": 2<br/>
}<br/>

### 커밋 (프로젝트)전체 통계<br/>
GET api/projects/{projectId}/commits/summary<br/>
성공 응답<br/>
{<br/>
"projectId": 2,<br/>
"totalCommits": 12,<br/>
"latestCommitDate": "2025-07-20T12:16:35",<br/>
"commitsThisWeek": 0,<br/>
"mostActiveDay": "Sunday"<br/>
}<br/>

### 커밋 위클리 집계<br/>
GET /api/projects/{projectId}/commits/weekly<br/>
성공 응답<br/>
[<br/>
{<br/>
"dayOfWeek": 4,<br/>
"count": 2<br/>
}<br/>
]<br/>

### 커밋 히스토리<br/>
GET /api/projects/{projectId}/commits/history<br/>
성공응답<br/>
[<br/>
{<br/>
"date": "2025-11-29",<br/>
"count": 2<br/>
},<br/>
{<br/>
"date": "2025-12-10",<br/>
"count": 2<br/>
}<br/>
]<br/>

### 커밋 전체 보기(전체지만 최근 30개만 가져와지고 필요 시 전체로 수정)<br/>
GET /api/projects/{projectId}/commits<br/>
성공응답<br/>
[{<br/>
"sha": "f857e0643fdca83b45a580cd6c257fd7a0980b21",<br/>
"message": "refactor:dangerlevel enum으로 변경",<br/>
"authorName": "simuneu",<br/>
"authorEmail": "ㄹㄹ@gmail.com",<br/>
"commitDate": "2025-11-23T08:24:53"<br/>
}]<br/>
<br/>
## user<br/>

### 비번 변경<br/><br/>
PATCH api/users/me/password<br/>
요청<br/>
{<br/>
"currentPassword": "abcd1234!",<br/>
"newPassword": "abcd1234!!",<br/>
"newPasswordConfirm": "abcd1234!!"<br/>
}<br/>