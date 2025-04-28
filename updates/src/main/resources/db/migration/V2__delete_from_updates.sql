CREATE OR REPLACE FUNCTION copy_to_history_on_delete()
    RETURNS TRIGGER AS
$$
BEGIN
    INSERT INTO update_history (id,
                                original_update_id,
                                operation_system_area,
                                description,
                                change_type)
    VALUES (uuid_generate_v4(),
            OLD.id,
            OLD.operation_system_area,
            OLD.description,
            'delete');
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trigger_copy_to_history_on_delete
    BEFORE DELETE
    ON updates
    FOR EACH ROW
EXECUTE PROCEDURE copy_to_history_on_delete();