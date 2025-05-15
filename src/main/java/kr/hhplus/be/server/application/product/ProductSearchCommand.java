package kr.hhplus.be.server.application.product;

public record ProductSearchCommand (
	String name,
	String category,
	String sortBy, // CATEGORY, PRICE, LATEST(최신순)
	String sorted, // DESC, ASC
	boolean soldOut
){
	public static ProductSearchCommand of(String name,String category,String sortBy,String sorted,boolean soldOut){
		return new ProductSearchCommand(name,category,sortBy,sorted,soldOut);
	}
}
