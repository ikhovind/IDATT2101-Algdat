# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.17

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:


#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:


# Disable VCS-based implicit rules.
% : %,v


# Disable VCS-based implicit rules.
% : RCS/%


# Disable VCS-based implicit rules.
% : RCS/%,v


# Disable VCS-based implicit rules.
% : SCCS/s.%


# Disable VCS-based implicit rules.
% : s.%


.SUFFIXES: .hpux_make_needs_suffix_list


# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

# Suppress display of executed commands.
$(VERBOSE).SILENT:


# A target that is always out of date.
cmake_force:

.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /opt/clion-2020.2/bin/cmake/linux/bin/cmake

# The command to remove a file.
RM = /opt/clion-2020.2/bin/cmake/linux/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9"

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9/cmake-build-debug"

# Include any dependencies generated for this target.
include CMakeFiles/Kapittel_9.dir/depend.make

# Include the progress variables for this target.
include CMakeFiles/Kapittel_9.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/Kapittel_9.dir/flags.make

CMakeFiles/Kapittel_9.dir/main.cpp.o: CMakeFiles/Kapittel_9.dir/flags.make
CMakeFiles/Kapittel_9.dir/main.cpp.o: ../main.cpp
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --progress-dir="/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/Kapittel_9.dir/main.cpp.o"
	/usr/bin/c++  $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -o CMakeFiles/Kapittel_9.dir/main.cpp.o -c "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9/main.cpp"

CMakeFiles/Kapittel_9.dir/main.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Preprocessing CXX source to CMakeFiles/Kapittel_9.dir/main.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9/main.cpp" > CMakeFiles/Kapittel_9.dir/main.cpp.i

CMakeFiles/Kapittel_9.dir/main.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green "Compiling CXX source to assembly CMakeFiles/Kapittel_9.dir/main.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9/main.cpp" -o CMakeFiles/Kapittel_9.dir/main.cpp.s

# Object files for target Kapittel_9
Kapittel_9_OBJECTS = \
"CMakeFiles/Kapittel_9.dir/main.cpp.o"

# External object files for target Kapittel_9
Kapittel_9_EXTERNAL_OBJECTS =

Kapittel_9: CMakeFiles/Kapittel_9.dir/main.cpp.o
Kapittel_9: CMakeFiles/Kapittel_9.dir/build.make
Kapittel_9: CMakeFiles/Kapittel_9.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color --switch=$(COLOR) --green --bold --progress-dir="/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_2) "Linking CXX executable Kapittel_9"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Kapittel_9.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/Kapittel_9.dir/build: Kapittel_9

.PHONY : CMakeFiles/Kapittel_9.dir/build

CMakeFiles/Kapittel_9.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/Kapittel_9.dir/cmake_clean.cmake
.PHONY : CMakeFiles/Kapittel_9.dir/clean

CMakeFiles/Kapittel_9.dir/depend:
	cd "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9/cmake-build-debug" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9" "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9" "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9/cmake-build-debug" "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9/cmake-build-debug" "/home/ingebrigt/Documents/uni - 2/Algoritmer og datastrukturer/Kapitler/Kapittel 9/cmake-build-debug/CMakeFiles/Kapittel_9.dir/DependInfo.cmake" --color=$(COLOR)
.PHONY : CMakeFiles/Kapittel_9.dir/depend

