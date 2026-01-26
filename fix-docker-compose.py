#!/usr/bin/env python3
"""Fix docker-compose.yml to remove postgres command and add database creation"""

with open('docker-compose.yml', 'r') as f:
    content = f.read()

# Remove the postgres command override lines
lines = content.split('\n')
new_lines = []
i = 0
while i < len(lines):
    if i == 19 and '# Initialize multiple databases' in lines[i]:
        # Skip this line and the next one
        i += 2
        continue
    new_lines.append(lines[i])
    i += 1

content = '\n'.join(new_lines)

# Fix the creator-api command
old_cmd = '      sh -c "python manage.py migrate &&'
new_cmd = '      sh -c "PGPASSWORD=postgres psql -h postgres -U postgres -tc \'SELECT 1 FROM pg_database WHERE datname = \'\'creator_portal\'\'\' | grep -q 1 || PGPASSWORD=postgres psql -h postgres -U postgres -c \'CREATE DATABASE creator_portal\' &&\n             python manage.py migrate &&'

content = content.replace(old_cmd, new_cmd)

with open('docker-compose.yml', 'w') as f:
    f.write(content)

print("✓ Fixed docker-compose.yml")
