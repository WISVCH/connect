SET AUTOCOMMIT FALSE;
START TRANSACTION;


INSERT INTO client_redirect_uri_TEMP (owner_id, redirect_uri) VALUES
  ('client', 'http://localhost:3000/oauth/callback');

INSERT INTO system_scope (scope, description, icon, restricted, default_scope) VALUES
  ('student', 'NetID and student number', 'book', FALSE, TRUE),
  ('auth', 'CH Auth information', 'lock', FALSE, TRUE);

MERGE INTO client_redirect_uri
USING (SELECT
         id,
         redirect_uri
       FROM client_redirect_uri_TEMP, client_details
       WHERE client_details.client_id = client_redirect_uri_TEMP.owner_id) AS vals(id, redirect_uri)
ON vals.id = client_redirect_uri.owner_id AND vals.redirect_uri = client_redirect_uri.redirect_uri
WHEN NOT MATCHED THEN
  INSERT (owner_id, redirect_uri) VALUES (vals.id, vals.redirect_uri);

MERGE INTO system_scope
USING (SELECT
         scope,
         description,
         icon,
         restricted,
         default_scope
       FROM system_scope_TEMP) AS vals(scope, description, icon, restricted, default_scope)
ON vals.scope = system_scope.scope
WHEN NOT MATCHED THEN
  INSERT (scope, description, icon, restricted, default_scope)
  VALUES (vals.scope, vals.description, vals.icon, vals.restricted, vals.default_scope);


COMMIT;
SET AUTOCOMMIT TRUE;
