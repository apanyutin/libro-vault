INSERT INTO books (id, title, price, isbn, author, description, cover_image, is_deleted) VALUES
(1, 'First book Title', 102.99, '978-9-166-21156-9', 'First book Author', 'First book Description', 'Cover image of first book', false),
(2, 'Second book Title', 922.55, '978-9-266-21156-9', 'Second book Author', 'Second book Description', 'Cover image of second book', false),
(3, 'Third book Title', 12.05, '978-9-366-21156-9', 'Third book Author', 'Third book Description', 'Cover image of third book', false);

INSERT INTO categories (id, name, description, is_deleted) VALUES
(1, 'First category Name', 'First category Description', false),
(2, 'Second category Name', 'Second category Description', false),
(3, 'Third category Name', 'Third category Description', false);

INSERT INTO books_categories (book_id, category_id) VALUES
(1, 1),  -- First book -> First category
(2, 1),  -- Second book -> First category
(2, 2),  -- Second book -> Second category
(3, 1),  -- Third book -> First category
(3, 3);  -- Third book -> Third category