#!/bin/sh

set -eu

APP_PATH="${1:?Informe o caminho do .app}"
MACOS_DIR="$APP_PATH/Contents/MacOS"
APP_DIR="$APP_PATH/Contents/app"
LAUNCHER_NAME="$(basename "$APP_PATH" .app)"
ORIGINAL_BIN="$MACOS_DIR/$LAUNCHER_NAME"
REAL_BIN="$MACOS_DIR/${LAUNCHER_NAME}-bin"
COMMAND_SCRIPT="$MACOS_DIR/${LAUNCHER_NAME}.command"
ORIGINAL_CFG="$APP_DIR/${LAUNCHER_NAME}.cfg"
REAL_CFG="$APP_DIR/${LAUNCHER_NAME}-bin.cfg"

if [ ! -f "$ORIGINAL_BIN" ]; then
  echo "Launcher nao encontrado em: $ORIGINAL_BIN" >&2
  exit 1
fi

if [ ! -f "$ORIGINAL_CFG" ]; then
  echo "Arquivo de configuracao nao encontrado em: $ORIGINAL_CFG" >&2
  exit 1
fi

mv "$ORIGINAL_BIN" "$REAL_BIN"
cp "$ORIGINAL_CFG" "$REAL_CFG"

cat > "$COMMAND_SCRIPT" <<EOF
#!/bin/sh

set -eu

SCRIPT_DIR="\$(cd "\$(dirname "\$0")" && pwd)"
"\$SCRIPT_DIR/${LAUNCHER_NAME}-bin"
/usr/bin/osascript -e 'tell application "Terminal" to close front window' >/dev/null 2>&1 || true
EOF

cat > "$ORIGINAL_BIN" <<EOF
#!/bin/sh

set -eu

SCRIPT_DIR="\$(cd "\$(dirname "\$0")" && pwd)"
REAL_BIN="\$SCRIPT_DIR/${LAUNCHER_NAME}-bin"

if [ -t 0 ] && [ -t 1 ]; then
  exec "\$REAL_BIN" "\$@"
fi

open -a Terminal "\$SCRIPT_DIR/${LAUNCHER_NAME}.command"
EOF

chmod +x "$ORIGINAL_BIN" "$REAL_BIN" "$COMMAND_SCRIPT"

/usr/bin/codesign --force --deep --sign - "$APP_PATH" >/dev/null
