package physicks.secondBoard.domain.author;

/**
 * <pre>
 * 게시글이나 댓글에서 사용되는 작성자(Author)가 Null 인 경우 발생합니다.
 * • 비회원(Guest) 인 경우 : 예외가 발생하지 않음. </li>
 * • 회원(User) 인 경우 : 데이터 결함으로 인해 Author 가 Null 인 경우 예외 발생 </li>
 * </pre>
 */
public class AuthorUserNullException extends NullPointerException {

    public AuthorUserNullException() {
    }

    public AuthorUserNullException(String s) {
        super(s);
    }
}
