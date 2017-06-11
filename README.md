# Simple Budget App

This app will allow you to record expenses from day to day.

It should work fine on most devices.

Currently the app only uses ZAR as the currency, I will try make this more universal in time.

## Current Features:

* Add expense quickly, with category
* Add new categories (but cannot remove, or re-arrange, they will always be alphabetical)
* Add notes on any expense
* View the expense from the 1st of this month
* View a summary of this month's expenses
* Export all data to JSON (which is almost a mirror of the internal DB)

## Permissions Required:

* `android.permission.WRITE_EXTERNAL_STORAGE` - Used to export the JSON file to primary external storage
* `android.permission.READ_EXTERNAL_STORAGE` - Not used yet, but might be used to import

## Planned future changes:

* Integration with Re-Hive <https://rehive.com> (will require internet permissions, and will be optional)
* View past month's data
* Compare previous months data to this month for trends
* Removal of categories (but I probably won't)
* Import of backup
* Backup to CSV for Excel
* Backup to E-Mail

## Icons come from:

* designed by Madebyoliver from Flaticon <http://www.flaticon.com/packs/essential-set-2>
* designed by Iconnice from Flaticon <http://www.flaticon.com/packs/the-ultimate/2>