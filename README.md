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