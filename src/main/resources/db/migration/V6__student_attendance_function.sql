CREATE OR REPLACE FUNCTION get_student_attendance(group_id_in varchar, student_id_in varchar)
    returns varchar
    language plpgsql
AS
$$
declare
    result_json varchar;
begin
    select '[' || string_agg(jsonTable.body, ',') || ']'
    into result_json
    from (select json_build_object('came', sa.came,
                                   'homeworkStatus', sa.homework_status,
                                   'attendanceDate', da.attendance_date):: text as body
          from student_attendance as sa
                   inner join daily_attendance da on da.id = sa.daily_attendance_id
          where da.group_id = group_id_in
            and sa.student_id = student_id_in order by da.attendance_date asc ) as jsonTable;

    return result_json;
end;
$$;