# Auth backend

## Routes

<!-- table -->
|----------|--------------|---------------------------|
| Method   | Url          | Arguments/Body data       |
|----------|--------------|---------------------------|
| `POST`   | /create-user | "body: json `User`"       |
| `GET`    | /get-user    | "&sub"                    |
| `UPDATE` | /update-user | "&sub, body: json `User`" |
| `DELETE` | /delete-user | "&sub"                    |
|----------|--------------|---------------------------|

In case of success, receive a json back, with the given format:
"{text:'message', code: 'code', data:'data'}"

## Data 

This is a typical `edn`, which holds the Application State related to the `user`.
``` clj
{:auth-result
   {:accessToken "accessToken",
    :appState nil,
    :expiresIn 7200,
    :idToken nil,
    :idTokenPayload nil,
    :refreshToken nil,
    :scope "openid profile email",
    :state "state-hash",
    :tokenType "Bearer"},
 :profile
   {:email "email",
    :email_verified true,
    :family_name "family-name",
    :given_name "given-name",
    :locale "en",
    :name "given-name family-name",
    :nickname "nickname",
    :picture "picture-url",
    :sub "google-oauth2|random-int-id",
    :updated_at "2024-02-20T16:05:34.533Z"}}
```
