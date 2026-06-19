INSERT INTO users (name, email) VALUES ('Alice Suporte', 'alice@helpdesk.com') ON CONFLICT DO NOTHING;
INSERT INTO users (name, email) VALUES ('Bob Admin', 'bob@helpdesk.com') ON CONFLICT DO NOTHING;
INSERT INTO users (name, email) VALUES ('Carlos TI', 'carlos@helpdesk.com') ON CONFLICT DO NOTHING;
