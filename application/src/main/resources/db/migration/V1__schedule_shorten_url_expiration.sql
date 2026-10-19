-- MySQL must have the global event_scheduler enabled for this event to run.
CREATE EVENT IF NOT EXISTS ev_expire_tb_shorten_url
    ON SCHEDULE EVERY 1 DAY
    STARTS CURRENT_DATE + INTERVAL 1 DAY
    DO
        UPDATE tb_shorten_url
        SET expired = TRUE,
            updated_at_date = CURRENT_TIMESTAMP
        WHERE DATE(created_at_date) <> CURRENT_DATE
          AND expired = FALSE;
