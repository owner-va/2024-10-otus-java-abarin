package hw;

import java.util.Map;
import java.util.TreeMap;

public class CustomerService {

    private final TreeMap<Customer, String> customersTreeMap = new TreeMap<>();

    public Map.Entry<Customer, String> getSmallest() {
        return copyEntry(customersTreeMap.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return copyEntry(customersTreeMap.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customersTreeMap.put(customer, data);
    }

    private Map.Entry<Customer, String> copyEntry(Map.Entry<Customer, String> customerEntry) {
        if (customerEntry != null) {
            var customerFromMap = customerEntry.getKey();
            var customer = new Customer(customerFromMap.getId(),
                    customerFromMap.getName(),
                    customerFromMap.getScores());
            return Map.entry(customer, customersTreeMap.get(customerFromMap));
        }
        return null;
    }
}
