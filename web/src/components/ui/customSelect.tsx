import React, {useEffect, useRef, useState} from "react";
import {ChevronDown} from "lucide-react";

interface CustomSelectProps<T> {
  options: { value: T; label: string }[];
  value: T | string;
  onChange: (value: T | string) => void;
  placeholder?: string;
}

const CustomSelect = <T extends React.Key>({
  options,
  value,
  onChange,
  placeholder = "Sélectionnez une option",
}: CustomSelectProps<T>) => {
  const [isOpen, setIsOpen] = useState(false);
  const [inputValue, setInputValue] = useState(value ? String(value) : "");
  const selectRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    setInputValue(value ? String(value) : "");
  }, [value]);

  const handleOptionClick = (optionValue: T) => {
    onChange(optionValue);
    setIsOpen(false);
  };

  const handleInputChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = event.target.value;
    setInputValue(newValue);
    onChange(newValue);
  };

  const handleBlur = () => {
    // Si la valeur saisie ne correspond à aucune option, la conserver
    if (!options.find((option) => String(option.value) === inputValue)) {
      onChange(inputValue);
    }
    setIsOpen(false);
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      event.preventDefault(); // Empêcher la soumission du formulaire
      handleBlur();
    }
  };

  const toggleOpen = () => {
    setIsOpen(!isOpen);
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        selectRef.current &&
        !selectRef.current.contains(event.target as Node)
      ) {
        setIsOpen(false);
      }
    };

    if (isOpen) {
      document.addEventListener("mousedown", handleClickOutside);
    }

    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [isOpen]);

  return (
    <div ref={selectRef} className="relative w-full">
      <div
        onClick={toggleOpen}
        className="flex h-10 w-full items-center justify-between rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50"
      >
        <input
          type="text"
          value={inputValue}
          onChange={handleInputChange}
          onBlur={handleBlur}
          onKeyDown={handleKeyDown}
          placeholder={placeholder}
          className="w-full bg-transparent focus:outline-none"
        />
        <ChevronDown className="h-4 w-4 opacity-50" />
      </div>
      {isOpen && (
        <div className="absolute z-10 mt-1 w-full rounded-md border bg-popover text-popover-foreground shadow-md max-h-48 overflow-y-auto">
          {options.map((option) => (
            <div
              key={option.value}
              onClick={() => handleOptionClick(option.value)}
              className="px-3 py-2 cursor-pointer hover:bg-accent hover:text-accent-foreground"
            >
              {option.label}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default CustomSelect;
