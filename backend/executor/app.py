from flask import Flask, request, jsonify
import subprocess
import tempfile
import os

app = Flask(__name__)

@app.route("/run", methods=["POST"])
def run_code():
    data = request.get_json()
    language = data.get("language", "java")
    code = data.get("code", "")
    input_data = data.get("input", "")

    if not code.strip():
        return jsonify({"error": "Empty code"}), 400

    # Create a temporary directory for code execution
    with tempfile.TemporaryDirectory() as tmpdir:
        if language.lower() == "java":
            source_file = os.path.join(tmpdir, "Main.java")
            with open(source_file, "w") as f:
                f.write(code)

            compile_proc = subprocess.run(
                ["javac", source_file],
                capture_output=True, text=True
            )

            if compile_proc.returncode != 0:
                return jsonify({
                    "stdout": "",
                    "stderr": compile_proc.stderr,
                    "exitCode": compile_proc.returncode
                })

            run_proc = subprocess.run(
                ["java", "-cp", tmpdir, "Main"],
                input=input_data, capture_output=True, text=True, timeout=5
            )

            return jsonify({
                "stdout": run_proc.stdout.strip(),
                "stderr": run_proc.stderr.strip(),
                "exitCode": run_proc.returncode
            })

        else:
            return jsonify({"error": f"Unsupported language: {language}"}), 400


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=6000)
