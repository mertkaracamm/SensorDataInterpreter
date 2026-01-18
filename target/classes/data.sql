-- Clear config table so as to avoid duplicate PK on every restart
DELETE FROM operational_config;


INSERT INTO operational_config (id, battery_charge_limit, voltage_window_minutes, voltage_change_percent)
VALUES (1, 20.0, 5, 10.0);

