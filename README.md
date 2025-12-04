api<br/>

#### 이메일 인증코드 발송<br/>
POST /api/auth/email/send<br/>
{
"email": "user@example.com"
}<br/>

#### 이메일 인증코드 확인<br/>

POST /api/auth/email/verify<br/>

{
"email": "user@example.com",
"code": "A1B2C3D4"
}<br/>

(이메일 템플릿 미적용)<br/>

#### 회원가입<br/>
POST /api/auth/signup<br/>

{
"email": "user@example.com",
"password" : "abcd1234!",
"nickname" :"다람띠"
}<br/>
조건 : 비밀번호는 영문, 숫자, 특수문자 포함 8자 이상<br/>

