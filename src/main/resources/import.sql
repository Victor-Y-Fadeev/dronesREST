INSERT INTO drones
	(serial_number, model, weight_limit, battery_capacity, state)
VALUES
	('0', 'H', 500, 100, 0),
	('1', 'M', 150, 80, 0),
	('2', 'C', 300, 5, 0),
	('3', 'L', 50, 90, 1),
	('4', 'C', 300, 100, 1),
	('5', 'M', 150, 90, 1),
	('6', 'L', 50, 75, 2),
	('7', 'L', 50, 85, 2),
	('8', 'H', 500, 95, 3),
	('9', 'L', 50, 70, 3),
	('10', 'M', 150, 50, 4),
	('11', 'C', 300, 65, 5);

INSERT INTO medications
	(name, weight, code, image)
VALUES
	('Pentalgin', 30, '0', NULL),
	('Mildronat', 250, '1', NULL),
	('Carsil', 35, '2', NULL),
	('Sinupret', 100, '3', NULL),
	('Phenibut', 250, '4', NULL),
	('Isoprinosine', 500, '5', NULL),
	('Enterosgel', 225, '6', NULL),
	('Pimafucin', 100, '7', NULL),
	('Clotrimazole', 10, '8', NULL),
	('Perineva', 75, '9', NULL),
	('Egilok', 25, '10', NULL),
	('Enap-H', 35, '11', NULL),
	('Aquadetrim', 15, '12', NULL);
