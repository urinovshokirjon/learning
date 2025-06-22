insert into profile (id, name, surname, phone, password, status, visible, created_date)
values ('8cb1eb8e-bd2e-4413-b4d1-adb35255b120', 'admin', 'adminjon', '998934569540', '827ccb0eea8a706c4c34a16891f84e7b','ACTIVE', true, current_timestamp);

insert into profile_role (id, profile_id, role, visible, created_date)
values ('8cb1eb8e-bd2e-4413-b4d1-adb35255b121', '8cb1eb8e-bd2e-4413-b4d1-adb35255b120' ,'ROLE_ADMIN', true,current_timestamp)on conflict (id) do nothing;
