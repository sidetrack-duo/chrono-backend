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

## github<br/>
### 유효한 깃허브 유저네임인지 조회<br/>
GET api/github/validate<br/>
http://localhost:8080/api/github/validate?username=simuneu<br/>
성공시<br/>
{<br/>
"valid": true,<br/>
"username": "simuneu",<br/>
"avatarUrl": "https://github.com/simuneu.png",<br/>
"message": "존재하는 GitHub 사용자입니다."<br/>
}<br/>
실패시<br/>
{<br/>
"valid": false,<br/>
"username": "simuneuffff",<br/>
"avatarUrl": null,<br/>
"message": "존재하지 않는 GitHub 사용자입니다."<br/>
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

