ALTER TABLE replica_parts ADD COLUMN tech_id int8;

ALTER TABLE replica_parts
    add constraint replica_part_tech foreign key (tech_id) references users;