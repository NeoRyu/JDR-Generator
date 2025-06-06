# ALTER TABLE character_context DROP COLUMN prompt_draw_style;

ALTER TABLE character_context
    ADD COLUMN prompt_draw_style VARCHAR(255)
        NOT NULL DEFAULT 'photoRealistic'
        AFTER prompt_class;