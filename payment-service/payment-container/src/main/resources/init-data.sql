INSERT INTO payment.credit_entry(id, customer_id, total_credit_amount)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb21', '1a36fd52-7929-11ed-a1eb-0242ac120002', 500.00);
INSERT INTO payment.credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb23', '1a36fd52-7929-11ed-a1eb-0242ac120002', 100.00, 'CREDIT');
INSERT INTO payment.credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb24', '1a36fd52-7929-11ed-a1eb-0242ac120002', 600.00, 'CREDIT');
INSERT INTO payment.credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb25', '1a36fd52-7929-11ed-a1eb-0242ac120002', 200.00, 'DEBIT');


INSERT INTO payment.credit_entry(id, customer_id, total_credit_amount)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb22', 'd215b5f8-0249-4dc5-89a3-51fd148cfb43', 100.00);
INSERT INTO payment.credit_history(id, customer_id, amount, type)
VALUES ('d215b5f8-0249-4dc5-89a3-51fd148cfb26', 'd215b5f8-0249-4dc5-89a3-51fd148cfb43', 100.00, 'CREDIT');
