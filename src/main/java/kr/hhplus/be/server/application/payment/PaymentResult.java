package kr.hhplus.be.server.application.payment;

public record PaymentResult (
	String token,
	String transactionId
){
	public static PaymentResult of(String token, String transactionId){
		return new PaymentResult(token, transactionId);
	}
}
