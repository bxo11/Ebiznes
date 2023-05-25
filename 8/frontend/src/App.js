import Products from "./ProductList";
import Cart from "./Cart";
import Product from './Product';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import AuthenticationPage, { action as authAction } from "./AuthPage";
import { checkAuthLoader, tokenLoader } from './auth';
import { action as logoutAction } from './Logout';

const router = createBrowserRouter([
  {
    path: '/',
    id: 'root',
    loader: tokenLoader,
    children: [
      { index: true,loader: checkAuthLoader, element: <Products />},
      { path: ':productId', id: 'product-detail',element: <Product />, loader: checkAuthLoader ,},
      { path: 'cart',element: <Cart />,loader: checkAuthLoader,},
      {
        path: 'auth',
        element: <AuthenticationPage />,
        action: authAction,
      },
      {
        path: 'logout',
        action: logoutAction,
      },
    ]
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
