import Products from "./ProductList";
import Product from "./Product";
import Cart from "./Cart";
import { loader as productLoader } from './Product';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';

const router = createBrowserRouter([
  {
    path: '/',
    children: [
      { index: true, element: <Products />},
      { path: ':productId', id: 'product-detail',element: <Product />, loader: productLoader,},
      { path: 'cart',element: <Cart />,}
    ]
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
