import React, { useState } from 'react';
import {useNavigate} from 'react-router-dom';

function Payments(props) {
  const navigate = useNavigate();
  const [paymentMethod, setPaymentMethod] = useState('');
  const cart = props.cart;

  const handlePayment = () => {
      fetch('http://localhost:8080/carts', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "Name": "Cart for test user",
            "Description": "Uer cart",
            "Products": cart
          })
       }
        )
        .then(response => response.json())
        .then(data => navigate('/'))
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
      <button onClick={handlePayment} disabled={cart.length === 0}>Pay</button>
    </div>
  );
}

export default Payments;
