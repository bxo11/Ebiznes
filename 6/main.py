import re
import unittest

from selenium import webdriver
from selenium.webdriver.common.by import By


class AppTests(unittest.TestCase):
    def setUp(self):
        self.driver = webdriver.Firefox()
        self.driver.get("http://localhost:3000")  # Replace with the actual URL of your app
        self.driver.implicitly_wait(1)

    def tearDown(self):
        self.driver.quit()

    def test_404_error_page(self):
        url = "http://localhost:3000/nonexistent-url/test"  # Replace with a non-existent URL of your app

        self.driver.get(url)

        body_text = self.driver.find_element(By.TAG_NAME, "body").text
        self.assertIn("404", body_text)
        self.assertIn("Not Found", body_text)

    def test_list_item_anchors_have_number_href(self):
        list_items = self.driver.find_elements(By.TAG_NAME, "li")

        for item in list_items:
            anchors = item.find_elements(By.TAG_NAME, "a")
            self.assertTrue(len(anchors) == 1)

            for anchor in anchors:
                href = anchor.get_attribute("href")
                self.assertTrue(int(href.split('/')[-1]) > 0)
                self.assertTrue(re.match(r".*/\d+$", href), f"Invalid href attribute: {href}")

    def test_cart_anchor_exists_and_is_clickable(self):
        cart_anchor = self.driver.find_element(By.XPATH, "//a[@href='/cart']")
        self.assertTrue(cart_anchor)
        self.assertTrue(cart_anchor.is_displayed())
        self.assertTrue(cart_anchor.is_enabled())
        self.assertTrue(cart_anchor.text == "Cart")

    def test_list_items_structure(self):
        list_items = self.driver.find_elements(By.TAG_NAME, "li")

        for item in list_items:
            button = item.find_element(By.TAG_NAME, "button")
            anchor = item.find_element(By.TAG_NAME, "a")
            anchor_div = anchor.find_element(By.TAG_NAME, "div")
            div_elements = anchor_div.find_elements(By.XPATH, "./*")

            self.assertIsNotNone(button)
            self.assertIsNotNone(anchor)
            self.assertTrue(anchor.get_attribute("href"))
            self.assertIsNotNone(anchor_div)
            self.assertEqual(len(div_elements), 3)

    def test_404_error_pages(self):
        endpoints = ["/0", "/notAnId"]

        for endpoint in endpoints:
            url = f"http://localhost:3000{endpoint}"
            self.driver.get(url)

            body_text = self.driver.find_element(By.TAG_NAME, "body").text
            self.assertIn("404", body_text)
            self.assertIn("Error", body_text)

    def test_empty_cart(self):
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")
        cart_button.click()

        list_items = self.driver.find_elements(By.TAG_NAME, "li")
        self.assertTrue(len(list_items) == 0)

    def test_add_to_cart_1_product(self):
        first_list_item = self.driver.find_element(By.XPATH, "//li[1]")
        add_to_cart_button = first_list_item.find_element(By.XPATH, "//button[contains(text(), 'Add to cart')]")
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")

        add_to_cart_button.click()
        cart_button.click()

        list_items = self.driver.find_elements(By.TAG_NAME, "li")
        self.assertTrue(len(list_items) == 1)

    def test_add_to_cart_multiple_products(self):
        list_items = self.driver.find_elements(By.XPATH, "//li")
        for item in list_items:
            add_to_cart_button = item.find_element(By.XPATH, "//button[contains(text(), 'Add to cart')]")
            add_to_cart_button.click()
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")
        cart_button.click()

        cart_list_items = self.driver.find_elements(By.XPATH, "//li")

        self.assertTrue(len(list_items) == len(cart_list_items))

    def test_add_to_cart_same_product_multiple_times(self):
        first_list_item = self.driver.find_element(By.XPATH, "//li[1]")
        add_to_cart_button = first_list_item.find_element(By.XPATH, "//button[contains(text(), 'Add to cart')]")
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")

        for _ in range(3):
            add_to_cart_button.click()

        cart_button.click()

        list_items = self.driver.find_elements(By.TAG_NAME, "li")
        self.assertTrue(len(list_items) == 3)

        text_set = set()
        for item in list_items:
            text = item.text
            text_set.add(text)

        self.assertEqual(len(text_set), 1)

    def test_cleaning_cart_after_going_back(self):
        first_list_item = self.driver.find_element(By.XPATH, "//li[1]")
        add_to_cart_button = first_list_item.find_element(By.XPATH, "//button[contains(text(), 'Add to cart')]")
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")

        add_to_cart_button.click()
        cart_button.click()

        list_items = self.driver.find_elements(By.TAG_NAME, "li")
        self.assertTrue(len(list_items) == 1)

        self.driver.back()
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")
        cart_button.click()
        list_items = self.driver.find_elements(By.TAG_NAME, "li")
        self.assertTrue(len(list_items) == 0)

    def test_disabled_payment(self):
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")
        cart_button.click()
        pay_button = self.driver.find_element(By.XPATH, "//button[contains(text(), 'Pay')]")
        is_disabled = pay_button.get_attribute("disabled")
        self.assertTrue(is_disabled)
        list_items = self.driver.find_elements(By.TAG_NAME, "li")
        self.assertTrue(len(list_items) == 0)

    def test_enabled_payment(self):
        first_list_item = self.driver.find_element(By.XPATH, "//li[1]")
        add_to_cart_button = first_list_item.find_element(By.XPATH, "//button[contains(text(), 'Add to cart')]")
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")

        add_to_cart_button.click()
        cart_button.click()
        pay_button = self.driver.find_element(By.XPATH, "//button[contains(text(), 'Pay')]")
        is_disabled = pay_button.get_attribute("disabled")
        self.assertIsNone(is_disabled)
        list_items = self.driver.find_elements(By.TAG_NAME, "li")
        self.assertTrue(len(list_items) > 0)

    def test_navigating_to_home_after_payment(self):
        first_list_item = self.driver.find_element(By.XPATH, "//li[1]")
        add_to_cart_button = first_list_item.find_element(By.XPATH, "//button[contains(text(), 'Add to cart')]")
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")

        add_to_cart_button.click()
        cart_button.click()
        pay_button = self.driver.find_element(By.XPATH, "//button[contains(text(), 'Pay')]")
        pay_button.click()
        current_url = self.driver.current_url
        self.assertEqual(current_url, "http://localhost:3000/")
        body_text = self.driver.find_element(By.TAG_NAME, "body").text
        self.assertIn("Product", body_text)

    def test_page_accessibility(self):
        urls = ["/", "/1"]

        for url in urls:
            self.driver.get("http://localhost:3000" + url)
            body_text = self.driver.find_element(By.TAG_NAME, "body").text
            self.assertNotIn("404", body_text)
            self.assertNotIn("500", body_text)

    def test_cart_page_contains_payment_method_text_and_select_box(self):
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")
        cart_button.click()
        payment_method_text = "Select payment method:"
        select_box_locator = (By.TAG_NAME, "select")

        # Check if payment method text is present
        payment_method_text_element = self.driver.find_element(By.XPATH,
                                                               f"//*[contains(text(), '{payment_method_text}')]")
        self.assertTrue(payment_method_text_element.is_displayed())

        # Check if select box is present
        select_box_element = self.driver.find_element(*select_box_locator)
        self.assertTrue(select_box_element.is_displayed())
        self.assertTrue(select_box_element.is_enabled())

    def test_select_contains_options(self):
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")
        cart_button.click()
        select_locator = (By.TAG_NAME, "select")
        expected_options = ["Select", "Credit Card", "PayPal"]

        select_element = self.driver.find_element(*select_locator)
        options = select_element.find_elements(By.TAG_NAME, "option")

        option_texts = [option.text for option in options]

        self.assertEqual(len(options), len(expected_options))

        for expected_option in expected_options:
            self.assertIn(expected_option, option_texts)

    def test_options_values_in_select_element(self):
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")
        cart_button.click()
        select_locator = (By.TAG_NAME, "select")
        expected_options = [
            {"name": "Select", "value": ""},
            {"name": "Credit Card", "value": "Credit Card"},
            {"name": "PayPal", "value": "PayPal"}
        ]

        select_element = self.driver.find_element(*select_locator)
        options = select_element.find_elements(By.TAG_NAME, "option")

        self.assertEqual(len(options), len(expected_options))

        for option, expected_option in zip(options, expected_options):
            option_text = option.text
            option_value = option.get_attribute("value")

            self.assertEqual(option_text, expected_option["name"])
            self.assertEqual(option_value, expected_option["value"])

    def test_select_box_not_required_for_pay_button(self):
        self.driver.implicitly_wait(3)
        first_list_item = self.driver.find_element(By.XPATH, "//li[1]")
        add_to_cart_button = first_list_item.find_element(By.XPATH, "//button[contains(text(), 'Add to cart')]")
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")

        add_to_cart_button.click()
        cart_button.click()
        pay_button = self.driver.find_element(By.XPATH, "//button[contains(text(), 'Pay')]")

        select_locator = (By.TAG_NAME, "select")
        pay_button_locator = (By.XPATH, "//button[contains(text(), 'Pay')]")

        select_element = self.driver.find_element(*select_locator)
        self.assertTrue(select_element.is_displayed())
        pay_button_element = self.driver.find_element(*pay_button_locator)

        select_required_attribute = select_element.get_attribute("required")
        pay_button_disabled_attribute = pay_button_element.get_attribute("disabled")

        self.assertIsNone(select_required_attribute)
        self.assertIsNone(pay_button_disabled_attribute)
        pay_button.click()

    def test_list_items_do_not_contain_buttons_and_anchors(self):
        list_items = self.driver.find_elements(By.XPATH, "//li")
        for item in list_items:
            add_to_cart_button = item.find_element(By.XPATH, "//button[contains(text(), 'Add to cart')]")
            add_to_cart_button.click()
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")
        cart_button.click()

        list_items = self.driver.find_elements(By.XPATH, "//li")
        for item in list_items:
            buttons = item.find_elements(By.TAG_NAME, "button")
            anchors = item.find_elements(By.TAG_NAME, "a")

            self.assertEqual(len(buttons), 0)
            self.assertEqual(len(anchors), 0)

    def test_cart_page(self):
        self.driver.get("http://localhost:3000/cart")
        body_text = self.driver.find_element(By.TAG_NAME, "body").text
        self.assertIn("Cart", body_text)
        self.assertNotIn("404", body_text)
        self.assertNotIn("500", body_text)

    def test_list_items_contain_div_with_two_elements(self):
        list_items = self.driver.find_elements(By.XPATH, "//li")
        for item in list_items:
            add_to_cart_button = item.find_element(By.XPATH, "//button[contains(text(), 'Add to cart')]")
            add_to_cart_button.click()
        cart_button = self.driver.find_element(By.LINK_TEXT, "Cart")
        cart_button.click()

        list_items = self.driver.find_elements(By.XPATH, "//li")
        for item in list_items:
            divs = item.find_elements(By.TAG_NAME, "div")

            self.assertEqual(len(divs), 1)
            self.assertTrue(item.is_displayed())

            div = divs[0]
            child_elements = div.find_elements(By.XPATH, "*")

            self.assertEqual(len(child_elements), 2)


if __name__ == '__main__':
    unittest.main()
