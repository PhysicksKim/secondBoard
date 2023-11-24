/**
 * <h1>login redirect 처리를 위한 패키지</h1>
 * 사용자가 login 페이지로 이동하거나, login 페이지로 유도된 경우(권한이 필요한 페이지 접근 시) login 성공 후 이전 페이지로 redirect 가 필요합니다.
 * Spring Security 에서는 RequestCache 를 이용한 redirect 방법을 제공합니다.
 * 하지만 이 방식만 사용해서는 modal login 요청에 대해서 redirect 작업을 처리할 수 없습니다.
 * 따라서 login redirect 를 처리하기 위해서 추가적인 작업이 필요로 합니다.
 * 더불어 사용자는 다양한 경로를 통해서 login 페이지로 유도될 수 있으므로, 각종 handler 들을 구현하고 이를 Spring Security 에 등록해야 합니다.
 * 이와 관련된 클래스들을 모두 이 package 에서 관리합니다.
 * <br><br>
 * <h2>사용자의 login 접근 케이스</h2>
 * - 케이스를 분리하는 이유 <br>
 * 사용자가 권한이 필요한 페이지로 접근했지만 권한이 없는 경우 AuthenticationEntryPoint 가 호출된 뒤 login page url 로 forward 됩니다.
 * 이 때, forward 되었기 때문에 login page controller 의 request 에서는 이전 페이지 정보를 referer 또는 requestURI 로 얻을 수 없습니다.
 * 따라서 AuthenticationException 이 발생한 경우에는 AuthenticationEntryPoint 에서 redirect url 을 추가해야 합니다.
 * <br><br>
 * <h3>Case A. AuthenticationException 이 발생한 경우</h3>
 * : 인증이 필요한 페이지로 접근했지만 권한이 없는 경우
 * <pre>
 *     1. 인증이 필요한 페이지 주소로 직접 접근
 *     2. 인증이 필요한 페이지 링크 클릭
 * </pre>
 * <h3>Case B. AuthenticationException 발생하지 않는 경우</h3>
 * <pre>
 *     1. 주소를 입력해서 로그인 페이지로 직접 접근
 *     2. 로그인 페이지로 이동 버튼을 눌러서 접근
 *     3. modal 로그인 실패
 *     4. login 페이지에서 로그인 실패
 * </pre>
 * <br>
 * <h2>보안 주의</h2>
 * url 을 통한 redirect 방식은 위험할 수 있으므로 주의해서 사용해야 합니다.
 * <br>
 * <a href="https://cheatsheetseries.owasp.org/cheatsheets/Unvalidated_Redirects_and_Forwards_Cheat_Sheet.html">OWASP Cheat Sheet</a>.
 */

package physicks.secondBoard.domain.member.login.redirect;