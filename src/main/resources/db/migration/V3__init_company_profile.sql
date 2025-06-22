insert into profile (id, name, surname, phone, password, status, visible, created_date)
values ('8cb1eb8e-bd2e-4413-b4d1-fdb35255b145', 'company', 'companyjon', '998910826696', '827ccb0eea8a706c4c34a16891f84e7b','ACTIVE', true, current_timestamp);

insert into profile_role (id, profile_id, role, visible, created_date)
values ('8cb1eb8e-bd2e-4413-b4d1-gdb35255b644', '8cb1eb8e-bd2e-4413-b4d1-fdb35255b145' ,'ROLE_COMPANY_MANAGER', true,current_timestamp)on conflict (id) do nothing;
