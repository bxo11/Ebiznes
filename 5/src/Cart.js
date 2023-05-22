import React from 'react';
import { useLocation } from 'react-router-dom';
import classes from './ProductList.module.css';
import Payments from './Payments';

const generateKey = (product) => {
  // Generate a random string of characters
  const randomString = Math.random().toString(36).substring(7);
  return `${product.ID}_${randomString}`;
};

const Cart = () => {
  const location = useLocation();
  const cartProducts = location.state;

  return (
    <div className={classes.products}>
      <h1>Cart</h1>
      <ul className={classes.list}>
        {cartProducts.map((product) => (
          <li key={generateKey(product)} className={classes.item}>
            <div className={classes.content}>
              <h2>{product.Name}</h2>
              <h3>{product.Price} $</h3>
            </div>
          </li>
        ))}
      </ul>
      <Payments cart={cartProducts}></Payments>
    </div>
  );
}

export default Cart;
