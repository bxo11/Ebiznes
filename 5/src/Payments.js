import React, { useState } from 'react';

function Payments(props) {
  const [paymentMethod, setPaymentMethod] = useState('');

  const handlePayment = () => {
      fetch('http://localhost:8080/carts', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "Name": "Cart for test user",
            "Description": "Uer cart",
            "Products": props.cart
          })
       }
        )
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.error('Error:', error));

  };

  return (
    <div>
      <h1>Payments</h1>
      <label>
        Select payment method:
        <select
          value={paymentMethod}
          onChange={(e) => setPaymentMethod(e.target.value)}
        >
          <option value="">Select</option>
          <option value="Credit Card">Credit Card</option>
          <option value="PayPal">PayPal</option>
        </select>
      </label>
      <button onClick={handlePayment}>Pay</button>
    </div>
  );
}

export default Payments;
