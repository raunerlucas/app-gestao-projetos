import {Component, EventEmitter, forwardRef, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {debounceTime, distinctUntilChanged, Subject} from 'rxjs';

export interface SearchableOption {
  id: any;
  label: string;
  value: any;

  [key: string]: any;
}

@Component({
  selector: 'app-searchable-select',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './searchable-select.html',
  styleUrl: './searchable-select.css',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SearchableSelect),
      multi: true
    }
  ]
})
export class SearchableSelect implements OnInit, OnDestroy, ControlValueAccessor {
  @Input() options: SearchableOption[] = [];
  @Input() placeholder: string = 'Selecione...';
  @Input() searchPlaceholder: string = 'Buscar...';
  @Input() noResultsText: string = 'Nenhum resultado encontrado';
  @Input() disabled: boolean = false;
  @Input() loading: boolean = false;
  @Input() clearable: boolean = true;
  @Input() searchable: boolean = true;
  @Input() maxHeight: string = '200px';

  @Output() search = new EventEmitter<string>();
  @Output() selectionChange = new EventEmitter<SearchableOption | null>();

  isOpen = false;
  searchTerm = '';
  selectedOption: SearchableOption | null = null;
  filteredOptions: SearchableOption[] = [];
  private searchSubject = new Subject<string>();

  ngOnInit(): void {
    this.filteredOptions = [...this.options];

    // Configurar debounce para busca
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged()
    ).subscribe(term => {
      this.search.emit(term);
      this.filterOptions(term);
    });
  }

  ngOnDestroy(): void {
    this.searchSubject.complete();
  }

  // ControlValueAccessor implementation
  writeValue(value: any): void {
    if (value) {
      this.selectedOption = this.options.find(option => option.value === value) || null;
    } else {
      this.selectedOption = null;
    }
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  toggleDropdown(): void {
    if (this.disabled) return;

    this.isOpen = !this.isOpen;
    if (this.isOpen) {
      this.onTouched();
    }
  }

  closeDropdown(): void {
    this.isOpen = false;
    this.searchTerm = '';
    this.filteredOptions = [...this.options];
  }

  onSearchInput(event: Event): void {
    const target = event.target as HTMLInputElement;
    this.searchTerm = target.value;
    this.searchSubject.next(this.searchTerm);
  }

  selectOption(option: SearchableOption): void {
    this.selectedOption = option;
    this.onChange(option.value);
    this.selectionChange.emit(option);
    this.closeDropdown();
  }

  clearSelection(): void {
    this.selectedOption = null;
    this.onChange(null);
    this.selectionChange.emit(null);
  }

  updateOptions(newOptions: SearchableOption[]): void {
    this.options = newOptions;
    this.filteredOptions = [...newOptions];

    // Verificar se a opção selecionada ainda existe
    if (this.selectedOption && !newOptions.find(opt => opt.value === this.selectedOption?.value)) {
      this.clearSelection();
    }
  }

  // ControlValueAccessor
  private onChange = (value: any) => {
  };

  private onTouched = () => {
  };

  private filterOptions(term: string): void {
    if (!term.trim()) {
      this.filteredOptions = [...this.options];
      return;
    }

    this.filteredOptions = this.options.filter(option =>
      option.label.toLowerCase().includes(term.toLowerCase())
    );
  }

  // TrackBy function para otimizar performance do *ngFor
  trackByValue(index: number, option: SearchableOption): any {
    return option.value;
  }
}
