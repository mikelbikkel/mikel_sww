DELIMITER //

DROP PROCEDURE IF EXISTS gen_station_eel_one //
CREATE PROCEDURE gen_station_eel_one (ct_code VARCHAR(10))
SQL SECURITY INVOKER
COMMENT 
'Generates EELs from station identifiers for one city.'
BEGIN
  DECLARE station VARCHAR(100);

  DECLARE cur_position INT DEFAULT 1 ;
  DECLARE remainder VARCHAR(100);
  DECLARE cur_string VARCHAR(100);
  DECLARE delimiter_length TINYINT UNSIGNED;
  DECLARE pos TINYINT UNSIGNED;
  DECLARE pos_splitter INT;
  DECLARE complex_splitter INT;

  DECLARE done INT DEFAULT 0;
  DECLARE cur1 CURSOR FOR SELECT station_id
	                        FROM   station
													WHERE  SUBSTRING(station_id, 1, 3) = ct_code
													AND    STATION_TYPE IN ('REGULAR', 'COMPLEX');
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

  DROP TEMPORARY TABLE IF EXISTS tmp_eel_stat_one;
  CREATE TEMPORARY TABLE tmp_eel_stat_one (
    station_id VARCHAR(100) NOT NULL
  , eel_word   VARCHAR(100) NOT NULL
	, idx        TINYINT UNSIGNED
  ) ENGINE=InnoDB;

  SET delimiter_length = CHAR_LENGTH('_');
  OPEN cur1;
 
  readloop: LOOP
    FETCH cur1 INTO station;
    IF done THEN
      LEAVE readloop;
    END IF;

    SET remainder = SUBSTRING(station, 4); -- Remove 3 char city code prefix.
    SET cur_position = 1;
		SET pos = 1;

		-- Use _X__X_ as splitter for complex suffix
		SET complex_splitter = INSTR(remainder, '_X__X_');
		IF complex_splitter > 0 THEN
			SET remainder = LEFT(remainder, complex_splitter - 1);
		END IF; 

    IF ct_code = 'NYC' OR ct_code = 'CCG' THEN
		  -- Use ___ as splitter for new york and chicago.
      SET pos_splitter = INSTR(remainder, '___');
      IF pos_splitter > 0 THEN
        SET remainder = LEFT(remainder, pos_splitter - 1);
      END IF; 
    END IF;
    
  	WHILE CHAR_LENGTH(remainder) > 0 AND cur_position > 0 DO
			SET cur_position = INSTR(remainder, '_');
			IF cur_position = 0 THEN
				SET cur_string = remainder;
			ELSE
				SET cur_string = LEFT(remainder, cur_position - 1);
			END IF;
			IF TRIM(cur_string) != '' THEN
				INSERT INTO tmp_eel_stat_one VALUES (station, cur_string, pos);
				SET pos = pos + 1;
			END IF;
			SET remainder = SUBSTRING(remainder, cur_position + delimiter_length);
		END WHILE;
  END LOOP;

  CLOSE cur1;

  DELETE FROM station_eel WHERE SUBSTRING(station_id, 1, 3) = ct_code;
  INSERT INTO station_eel 
    SELECT DISTINCT tmp.station_id, SUBSTRING(tmp.eel_word, 1, 1)
    FROM   tmp_eel_stat_one tmp
    WHERE  NOT EXISTS (SELECT 'x'
                       FROM   eel_noise_words ns
                       WHERE  (ns.noise_word = tmp.eel_word
                               AND ns.city_code IS NULL
															 AND tmp.idx > 1)
                       OR     (ns.noise_word = tmp.eel_word
                               AND ns.city_code = SUBSTRING(tmp.station_id, 1, 3)
															 AND tmp.idx > 1)
                       );
END //


DROP PROCEDURE IF EXISTS gen_landmark_eel_one //
CREATE PROCEDURE gen_landmark_eel_one (ct_code VARCHAR(10))
SQL SECURITY INVOKER
COMMENT 
'Generates EELs from landmark identifiers for 1 city.'
BEGIN
  DECLARE v_location_id VARCHAR(100);

  DECLARE v_cur_position INT DEFAULT 1;
  DECLARE v_remainder VARCHAR(100);
  DECLARE v_cur_string VARCHAR(100);
  DECLARE v_delimiter_length TINYINT UNSIGNED;

  DECLARE v_done INT DEFAULT 0;
  DECLARE v_cur1 CURSOR FOR SELECT landmark_id
	                          FROM landmark
													  WHERE  SUBSTRING(landmark_id, 1, 3) = ct_code;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

  DROP TEMPORARY TABLE IF EXISTS tmp_eel_land_one;
  CREATE TEMPORARY TABLE tmp_eel_land_one (
    landmark_id VARCHAR(100) NOT NULL
  , eel_word    VARCHAR(100) NOT NULL
  ) ENGINE=InnoDB;

  SET v_delimiter_length = CHAR_LENGTH('_');
  OPEN v_cur1;
 
  readloop: LOOP
    FETCH v_cur1 INTO v_location_id;
    IF v_done THEN
      LEAVE readloop;
    END IF;

    -- Remove 3 char city code prefix.
    SET v_remainder = SUBSTRING(v_location_id, 4); 
    SET v_cur_position = 1;

    WHILE CHAR_LENGTH(v_remainder) > 0 AND v_cur_position > 0 DO
      SET v_cur_position = INSTR(v_remainder, '_');
      IF v_cur_position = 0 THEN
        SET v_cur_string = v_remainder;
      ELSE
        SET v_cur_string = LEFT(v_remainder, v_cur_position - 1);
      END IF;
      IF TRIM(v_cur_string) != '' THEN
        INSERT INTO tmp_eel_land_one VALUES (v_location_id, v_cur_string);
      END IF;
      SET v_remainder = SUBSTRING(v_remainder, v_cur_position + v_delimiter_length);
    END WHILE;
  END LOOP;

  CLOSE v_cur1;

  DELETE FROM landmark_eel WHERE SUBSTRING(landmark_id, 1, 3) = ct_code;
  INSERT INTO landmark_eel 
    SELECT DISTINCT tel.landmark_id, SUBSTRING(tel.eel_word, 1, 1)
    FROM   tmp_eel_land_one tel
    WHERE  NOT EXISTS (SELECT 'x'
                       FROM   eel_noise_words ns
                       WHERE  ns.noise_word = tel.eel_word);
END //

DROP PROCEDURE IF EXISTS check_city_id_code //
CREATE PROCEDURE check_city_id_code (ct_id VARCHAR(25), ct_code VARCHAR(10))
SQL SECURITY INVOKER
COMMENT
'Check if city_code and city_id combination is valid.'
BEGIN
  DECLARE des VARCHAR(100);

  DECLARE cur1 CURSOR FOR SELECT description FROM city WHERE city_id = ct_id AND city_code = ct_code;

  DECLARE cur666 CURSOR FOR SELECT description FROM non_existing_table;

  DECLARE CONTINUE HANDLER FOR NOT FOUND BEGIN OPEN cur666; END;

  OPEN cur1;
  FETCH cur1 INTO des;
  CLOSE cur1;

END //

DROP PROCEDURE IF EXISTS gen_station_eel //
CREATE PROCEDURE gen_station_eel ()
SQL SECURITY INVOKER
COMMENT 
'Generates EELs from station identifiers.'
BEGIN
  DECLARE done INT DEFAULT 0;
  DECLARE v_city_code VARCHAR(10);
  DECLARE cur1 CURSOR FOR SELECT city_code FROM city WHERE has_line_info = 'Y';
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

  OPEN cur1;
  readloop: LOOP
    FETCH cur1 INTO v_city_code;
    IF done THEN
      LEAVE readloop;
    END IF;
		CALL gen_station_eel_one(v_city_code);
  END LOOP;
END //

DROP PROCEDURE IF EXISTS gen_landmark_eel //
CREATE PROCEDURE gen_landmark_eel ()
SQL SECURITY INVOKER
COMMENT 
'Generates EELs from landmark identifiers.'
BEGIN
  DECLARE v_location_id VARCHAR(100);

  DECLARE v_cur_position INT DEFAULT 1;
  DECLARE v_remainder VARCHAR(100);
  DECLARE v_cur_string VARCHAR(100);
  DECLARE v_delimiter_length TINYINT UNSIGNED;

  DECLARE v_done INT DEFAULT 0;
  DECLARE v_cur1 CURSOR FOR SELECT landmark_id FROM landmark;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_done = 1;

  DROP TEMPORARY TABLE IF EXISTS tmp_eel_land;
  CREATE TEMPORARY TABLE tmp_eel_land (
    landmark_id VARCHAR(100) NOT NULL
  , eel_word    VARCHAR(100) NOT NULL
  ) ENGINE=InnoDB;

  SET v_delimiter_length = CHAR_LENGTH('_');
  OPEN v_cur1;
 
  readloop: LOOP
    FETCH v_cur1 INTO v_location_id;
    IF v_done THEN
      LEAVE readloop;
    END IF;

    -- Remove 3 char city code prefix.
    SET v_remainder = SUBSTRING(v_location_id, 4); 
    SET v_cur_position = 1;

    WHILE CHAR_LENGTH(v_remainder) > 0 AND v_cur_position > 0 DO
      SET v_cur_position = INSTR(v_remainder, '_');
      IF v_cur_position = 0 THEN
        SET v_cur_string = v_remainder;
      ELSE
        SET v_cur_string = LEFT(v_remainder, v_cur_position - 1);
      END IF;
      IF TRIM(v_cur_string) != '' THEN
        INSERT INTO tmp_eel_land VALUES (v_location_id, v_cur_string);
      END IF;
      SET v_remainder = SUBSTRING(v_remainder, v_cur_position + v_delimiter_length);
    END WHILE;
  END LOOP;

  CLOSE v_cur1;

  DELETE FROM landmark_eel;
  INSERT INTO landmark_eel 
    SELECT DISTINCT tel.landmark_id, SUBSTRING(tel.eel_word, 1, 1)
    FROM   tmp_eel_land tel
    WHERE  NOT EXISTS (SELECT 'x'
                       FROM   eel_noise_words ns
                       WHERE  ns.noise_word = tel.eel_word);
END //

DELIMITER ;

/* 
CALL gen_station_eel();
CALL gen_landmark_eel();
*/

