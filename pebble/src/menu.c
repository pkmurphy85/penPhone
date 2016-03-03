#include <pebble.h>
#include "menu.h"
#include "prerun.h"
	
// Constants
#define NUMBER_OF_SECTIONS 1
#define NUMBER_OF_ITEMS 11

// Global variables
/*
static Window *window;

void menu_init(){
	window = window_create();
	
	Layer *window_layer = window_get_root_layer(window);
	GRect bounds = layer_get_bounds(window_layer);
	prerun_init();
	
	window_stack_push(window, true);
}

void menu_deinit(){
	prerun_deinit();
	
	window_destroy(window);
	APP_LOG(APP_LOG_LEVEL_DEBUG, "Finished menu");
}*/
