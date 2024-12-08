# team026-DayTa
This is a template for CS411 project repository. Please make sure that your title follows the convention: [TeamID]-[YourTeamName]. All TeamIDs should have a three-digit coding (i.e. if you are team 20, you should have team020 as your ID.). You should also ensure that your URL for this repository is [sp24-cs411-team000-teamname.git] so TAs can correctly clone your repository and keep it up-to-date.

## Permission
Make your repository private. TAs will be able to access it even if it's private.

## Preparing for your release
Each submission should be in its own release. Release are specific freezes to your repository. You should submit your commit hash on PrairieLearn. When tagging your stage, please use the tag stage.x where x is the number to represent the stage.

## Keeping things up-to-date
Please make sure you keep your project root files up-to-date. Information for each file/folder are explained.

## Code Contribution
Individual code contributions will be used to evaluate individual contributions to the project.

## Store Procedure
DELIMITER //

CREATE PROCEDURE `SearchJobsByKeyword`(
    IN keyword VARCHAR(255),
    IN remoteOnly TINYINT
)
BEGIN
    IF remoteOnly = 1 THEN
        SELECT * 
        FROM job 
        WHERE (LOWER(title) LIKE CONCAT('%', keyword, '%') 
               OR LOWER(category) LIKE CONCAT('%', keyword, '%'))
          AND LOWER(type) = 'remote';
    ELSE
        SELECT * 
        FROM job 
        WHERE LOWER(title) LIKE CONCAT('%', keyword, '%') 
           OR LOWER(category) LIKE CONCAT('%', keyword, '%');
    END IF;
END //

DELIMITER ;

## Transaction
CREATE PROCEDURE PerformJobTransaction(
    IN title VARCHAR(255),
    IN category VARCHAR(255),
    IN remoteOnly BOOLEAN
)
BEGIN
    //Step 1: Insert a new job
    INSERT INTO Job (CompanyId, Title, Category, Location, Duration, Type, SkillsKeyWord)
    VALUES (1, title, category, 'Remote', 12, IF(remoteOnly, 'remote', 'hybrid'), 'Java, Spring Boot');

    //Step 2: Update an existing job
    UPDATE Job
    SET Duration = Duration + 6
    WHERE JobId = 1;

    // Step 3: Log the transaction
    INSERT INTO search_log (keyword, remote_only, result_count, timestamp)
    VALUES (title, remoteOnly, 1, NOW());
END


