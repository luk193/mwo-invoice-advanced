package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
	private int number;
	private static int  nextNumber =0;
	
	

	public Invoice() {
		this.number = nextNumber ;
		nextNumber++;
		

	}
	
	

	public int getInvoiceNumber() {
		return this.number;
	}

	private Map<Product, Integer> products = new HashMap<Product, Integer>();

	public void addProduct(Product product) {
		addProduct(product, 1);
	}

	public void addProduct(Product product, Integer quantity) {
		if (product == null || quantity <= 0) {
			throw new IllegalArgumentException();
		}
		if (products.containsKey(product)){
			products.put(product, products.get(product) +quantity);
		}
		else{
			products.put(product, quantity);
		}
	}

	public BigDecimal getNetTotal() {
		BigDecimal totalNet = BigDecimal.ZERO;
		for (Product product : products.keySet()) {
			BigDecimal quantity = new BigDecimal(products.get(product));
			totalNet = totalNet.add(product.getPrice().multiply(quantity));
		}
		return totalNet;
	}

	public BigDecimal getTaxTotal() {
		return getGrossTotal().subtract(getNetTotal());
	}

	public BigDecimal getGrossTotal() {
		BigDecimal totalGross = BigDecimal.ZERO;
		for (Product product : products.keySet()) {
			BigDecimal quantity = new BigDecimal(products.get(product));
			totalGross = totalGross.add(product.getPriceWithTax().multiply(quantity));
		}
		return totalGross;
	}

	public String printedVersion() {
		String printed = String.valueOf(number);
		for (Product product: products.keySet()){
			printed += "\n" + product.getName();
			printed += ", " + product.getClass().getName();
			printed += ", " + products.get(product);
			
			
		}
		printed += "\n "+ "Liczba produktow: "+ products.size();
		
		return printed;
		
		
	}
}
