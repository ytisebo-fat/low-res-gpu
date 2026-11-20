#!/bin/bash
# macOS only. Saves the Jagex account login that the Jagex Launcher hands to RuneLite, so that
# run.command can log in to the development client.
#
# Why: on macOS the Jagex Launcher passes extra arguments to RuneLite that replace the
# --insecure-write-credentials client argument, so the usual credentials.properties never gets written.
#
# How to use:
#   1. Start Old School RuneScape with RuneLite from the Jagex Launcher and wait for the login screen.
#   2. Double-click this file. RuneLite can be closed afterwards.
OUT="$HOME/.runelite/jagex-login.env"

# The Jagex Launcher gives RuneLite the login as JX_* environment variables; read them from the running process
LINE="$(ps -wwEx -o command= | grep 'JX_SESSION_ID=' | grep -v grep | head -1)"
if [ -z "$LINE" ]; then
	echo "Could not find a RuneLite that was started by the Jagex Launcher."
	echo "Start Old School RuneScape with RuneLite from the Jagex Launcher, wait for the login screen,"
	echo "then run this again."
	read -r -p "Press Enter to close..."
	exit 1
fi

mkdir -p "$HOME/.runelite"
umask 077
# Values can contain spaces (display names), so a value runs until the next " NAME=" or the end of the line
printf '%s\n' "$LINE" | perl -ne '
	while (/(?:^| )(JX_[A-Z_]+)=(.*?)(?= [A-Za-z_][A-Za-z0-9_]*=|$)/g) { $v{$1} = $2 }
	END {
		for my $k (sort keys %v) {
			(my $x = $v{$k}) =~ s/\x27/\x27\\\x27\x27/g;
			print "export $k=\x27$x\x27\n";
		}
	}
' > "$OUT"
chmod 600 "$OUT"

echo "Saved your Jagex login for the development client to:"
echo "  $OUT"
echo "Contains: $(sed -E 's/^export ([A-Z_]+)=.*/\1/' "$OUT" | tr '\n' ' ')"
echo
echo "Now start run.command. Never share this file, and delete it when you're done testing:"
echo "  rm \"$OUT\""
read -r -p "Press Enter to close..."
