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