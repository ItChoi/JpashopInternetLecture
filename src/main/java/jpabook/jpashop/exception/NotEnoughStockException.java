package jpabook.jpashop.exception;

public class NotEnoughStockException extends RuntimeException {

    /**
     * 오버라이드 하는 이뉴는 메시지를 넘겨주고, 근원적 에러를 넣어서 익셉션 트레이서가 쭉 나오게 할 수 있다.
     */


    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

    // 얜 없어도 되겠다.
    /*public NotEnoughStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }*/

}
