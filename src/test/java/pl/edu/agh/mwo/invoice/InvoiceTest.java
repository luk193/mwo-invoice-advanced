package pl.edu.agh.mwo.invoice;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.product.DairyProduct;
import pl.edu.agh.mwo.invoice.product.OtherProduct;
import pl.edu.agh.mwo.invoice.product.Product;
import pl.edu.agh.mwo.invoice.product.TaxFreeProduct;

public class InvoiceTest {
	private static final String PRODUCT_1 = "Product 1";
	private static final String PRODUCT_2 = "Product 2";
	private static final String PRODUCT_3 = "Product 3";

	//@Before
	//public static void resetInvoiceNumber() {
		//Invoice.getInvoiceNumkber();

	//}

	@Test
	public void testEmptyInvoiceHasEmptySubtotal() {
		Invoice invoice = createEmptyInvoice();
		assertBigDecimalsAreEqual(BigDecimal.ZERO, invoice.getNetTotal());
	}

	@Test
	public void testEmptyInvoiceHasEmptyTaxAmount() {
		Invoice invoice = createEmptyInvoice();
		assertBigDecimalsAreEqual(BigDecimal.ZERO, invoice.getTaxTotal());
	}

	@Test
	public void testEmptyInvoiceHasEmptyTotal() {
		Invoice invoice = createEmptyInvoice();
		assertBigDecimalsAreEqual(BigDecimal.ZERO, invoice.getGrossTotal());
	}

	@Test
	public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 1);
		assertBigDecimalsAreEqual(invoice.getNetTotal(), invoice.getGrossTotal());
	}

	@Test
	public void testInvoiceHasProperSubtotalForManyProduct() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 1);
		invoice.addProduct(createOtherProduct(), 1);
		invoice.addProduct(createDairyProduct(), 1);
		assertBigDecimalsAreEqual("259.99", invoice.getNetTotal());
	}

	@Test
	public void testInvoiceHasProperTaxValueForManyProduct() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 1);
		invoice.addProduct(createOtherProduct(), 1);
		invoice.addProduct(createDairyProduct(), 1);
		assertBigDecimalsAreEqual("12.3", invoice.getTaxTotal());
	}

	@Test
	public void testInvoiceHasProperTotalValueForManyProduct() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct());
		invoice.addProduct(createOtherProduct());
		invoice.addProduct(createDairyProduct());
		assertBigDecimalsAreEqual("272.29", invoice.getGrossTotal());
	}

	@Test
	public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 3); // Subtotal: 599.97
		invoice.addProduct(createOtherProduct(), 2); // Subtotal: 100.00
		invoice.addProduct(createDairyProduct(), 4); // Subtotal: 40.00
		assertBigDecimalsAreEqual("739.97", invoice.getNetTotal());
	}

	@Test
	public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 3); // Total: 599.97
		invoice.addProduct(createOtherProduct(), 2); // Total: 123.00
		invoice.addProduct(createDairyProduct(), 4); // Total: 43.2
		assertBigDecimalsAreEqual("766.17", invoice.getGrossTotal());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithZeroQuantity() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvoiceWithNegativeQuantity() {
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(createTaxFreeProduct(), -1);
	}

	private Invoice createEmptyInvoice() {
		return new Invoice();
	}

	private Product createTaxFreeProduct() {
		return new TaxFreeProduct(PRODUCT_1, new BigDecimal("199.99"));
	}

	private Product createOtherProduct() {
		return new OtherProduct(PRODUCT_2, new BigDecimal("50.0"));
	}

	private Product createDairyProduct() {
		return new DairyProduct(PRODUCT_3, new BigDecimal("10.0"));
	}

	private void assertBigDecimalsAreEqual(String expected, BigDecimal actual) {
		assertEquals(new BigDecimal(expected).stripTrailingZeros(), actual.stripTrailingZeros());
	}

	private void assertBigDecimalsAreEqual(BigDecimal expected, BigDecimal actual) {
		assertEquals(expected.stripTrailingZeros(), actual.stripTrailingZeros());
	}

	@Test
	public void testInvoiceNumber() {
		Invoice invoice1 = createEmptyInvoice();
		Invoice invoice2 = createEmptyInvoice();
		Invoice invoice3 = createEmptyInvoice();

		Assert.assertThat(invoice1.getInvoiceNumber(), Matchers.greaterThan(-1));
		assertEquals(1, invoice2.getInvoiceNumber());
		assertEquals(2, invoice3.getInvoiceNumber());
	}

	@Test
	public void testTwoInvoicesHasdifferentNumber() {
		Invoice invoice1 = createEmptyInvoice();
		Invoice invoice2 = createEmptyInvoice();

		Assert.notequals(invoice1.getInvoiceNumber(), invoice2.getInvoiceNumber());

	}
	
	@Test
	public void testPrintedInvoiceHasNumber(){
		Invoice invoice = createEmptyInvoice();
		
		int number = invoice.getInvoiceNumber();
		String printedInvoice = invoice.printedVersion();
		Assert.assertThat(printedInvoice,Matchers.containsString(String.valueOf(number)));
		
	}
	
	@Test
	public void testPrintedInvoiceHasProductName(){
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(new DairyProduct("Mleko", new BigDecimal(100)));
		
		int number = invoice.getInvoiceNumber();
		String printedInvoice = invoice.printedVersion();
		Assert.assertThat(printedInvoice,Matchers.containsString(String.valueOf(number)));
		
	}
	
	@Test
	public void testPrintedInvoicee(){
		Invoice invoice = createEmptyInvoice();
		invoice.addProduct(new DairyProduct("Mleko", new BigDecimal(100)));
		invoice.addProduct(new DairyProduct("Mleko", new BigDecimal(200)));
		
		
		int number = invoice.getInvoiceNumber();
		String printedInvoice = invoice.printedVersion();
		Assert.assertThat(printedInvoice,Matchers.containsString("Liczba produktow: 2"));
		
	}

}
