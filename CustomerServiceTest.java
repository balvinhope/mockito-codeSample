package org.mockito.mockito_all;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.any;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.dao.CustomerDao;
import com.entity.Customer;

public class CustomerServiceTest {

	@Mock
	private CustomerDao daoMock;

	@InjectMocks
	private CustomerService service;

	@Captor
	private ArgumentCaptor<Customer> customerArgument;

	public CustomerServiceTest() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testRegister() {
		service.register(new Customer());
		verify(daoMock).save(customerArgument.capture());
		assertThat(customerArgument.getValue().getToken(), is(notNullValue()));
	}

	@Test
	public void testAddCustomer_returnsNewCustomer() {
		when(daoMock.save(any(Customer.class))).thenReturn(new Customer());
		Customer customer = new Customer();
		assertThat(service.addCustomer(customer), is(notNullValue()));
	}

	@Test
	public void testAddCustomer_throwsException() {
		when(daoMock.save(any(Customer.class))).thenThrow(RuntimeException.class);
		Customer customer = new Customer();
		assertThat(service.addCustomer(customer), is(nullValue()));
	}

	@Test(expected = RuntimeException.class)
	public void testUpdate_throwsException() {
		doThrow(RuntimeException.class).when(daoMock).updateEmail(any(Customer.class), any(String.class));

		// calling the method under test
		Customer customer = service.changeEmail("old@test.com", "new@test.com");

	}

}
